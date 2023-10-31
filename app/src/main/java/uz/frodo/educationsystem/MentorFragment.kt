package uz.frodo.educationsystem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.frodo.educationsystem.adapters.MentorAdapter
import uz.frodo.educationsystem.databinding.FragmentMentorBinding
import uz.frodo.educationsystem.databinding.MentorDialogBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.interfaces.OnClickMentor
import uz.frodo.educationsystem.model.Course
import uz.frodo.educationsystem.model.Mentor


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MentorFragment : Fragment(), OnClickMentor {
    lateinit var binding: FragmentMentorBinding
    lateinit var adapter: MentorAdapter
    lateinit var myDbHelper: MyDbHelper
    lateinit var course: Course
    lateinit var mainList: ArrayList<Mentor>
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
    ): View {
        binding = FragmentMentorBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(requireContext())
        mainList = ArrayList()
        course = myDbHelper.getCourseById(arguments?.getInt("Mentor")!!)
        binding.toolbarMentor.title = course.name

        mainList = (myDbHelper.getAllMentors(course.id))
        adapter = MentorAdapter(this)
        adapter.submitList(mainList)
        binding.rvMentor.adapter = adapter

        binding.mentorAdd.setOnClickListener {
            findNavController().navigate(R.id.addMentorFragment, bundleOf("courseID" to course.id))
        }

        binding.toolbarMentor.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


    override fun onEditClick(mentor: Mentor, position: Int) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val mBinding = MentorDialogBinding.inflate(layoutInflater)
        dialog.setView(mBinding.root)
        mBinding.mDialogE1.setText(mentor.surname)
        mBinding.mDialogE2.setText(mentor.name)
        mBinding.mDialogE3.setText(mentor.father)
        dialog.setCancelable(false)

        mBinding.mDialogP.setOnClickListener {
            val surname = mBinding.mDialogE1.text.toString().trim()
            val name = mBinding.mDialogE2.text.toString().trim()
            val father = mBinding.mDialogE3.text.toString().trim()
            if (surname.isNotBlank() && name.isNotBlank() && father.isNotBlank()) {
                val m = Mentor(mentor.id, mentor.course, name, surname, father)
                myDbHelper.updateMentor(m)
                mainList[position] = m
                adapter.submitList(mainList)
                binding.rvMentor.adapter = adapter
                dialog.dismiss()
            } else
                Snackbar.make(it, "Fill the blank", 2000).show()

        }
        mBinding.mDialogN.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onDeleteClick(mentor: Mentor) {
        myDbHelper.deleteMentor(mentor)
        mainList.remove(mentor)
        adapter.submitList(mainList)
        binding.rvMentor.adapter = adapter
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            MentorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}