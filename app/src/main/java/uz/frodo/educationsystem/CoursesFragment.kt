package uz.frodo.educationsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.frodo.educationsystem.adapters.CourseAdapter
import uz.frodo.educationsystem.databinding.CourseDialogBinding
import uz.frodo.educationsystem.databinding.FragmentCoursesBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.model.Course


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CoursesFragment : Fragment() {
    lateinit var binding: FragmentCoursesBinding
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoursesBinding.inflate(layoutInflater)
        val myDbHelper = MyDbHelper(requireContext())

        var next = arguments?.getString("which")
        if (next == "Course") binding.courseAdd.visibility = View.VISIBLE


         val adapter = CourseAdapter(object : CourseAdapter.CourseClick{
            override fun itemClick(course: Course) {
                when(next){
                    "Course" -> findNavController().navigate(R.id.aboutFragment, bundleOf("Course" to course.id))
                    "Group" -> findNavController().navigate(R.id.groupsFragment, bundleOf("Group" to course.id))
                    "Mentor" -> findNavController().navigate(R.id.mentorFragment, bundleOf("Mentor" to course.id))
                }
            }
        })
        adapter.submitList(myDbHelper.getAllCourses())
        binding.rvCourse.adapter = adapter


        binding.courseAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val courseBinding = CourseDialogBinding.inflate(layoutInflater)
            dialog.setView(courseBinding.root)
            dialog.setCancelable(false)

            courseBinding.courseDialogP.setOnClickListener {
                val name = courseBinding.courseDialogE1.text.toString().trim()
                val about = courseBinding.courseDialogE2.text.toString().trim()
                if (name.isNotBlank() && about.isNotBlank()){
                    val course = Course(0,name,about)
                    myDbHelper.insertCourse(course)
                    adapter.submitList(myDbHelper.getAllCourses())
                    binding.rvCourse.adapter = adapter
                    dialog.dismiss()
                }else
                    Snackbar.make(it,"Fill the blank",1000).show()
            }

            courseBinding.courseDialogN.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.toolbarMentorAdd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            CoursesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}