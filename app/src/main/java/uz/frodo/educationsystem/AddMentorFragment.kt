package uz.frodo.educationsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.frodo.educationsystem.databinding.FragmentAddMentorBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.model.Course
import uz.frodo.educationsystem.model.Mentor


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddMentorFragment : Fragment() {
    lateinit var binding: FragmentAddMentorBinding
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
        binding = FragmentAddMentorBinding.inflate(layoutInflater)
        val myDbHelper = MyDbHelper(requireContext())

        val course = myDbHelper.getCourseById(arguments?.getInt("courseID")!!)



        binding.buttonAdd.setOnClickListener {
            val surname = binding.mentorSurname.text.toString().trim()
            val name = binding.mentorName.text.toString().trim()
            val father = binding.mentorFather.text.toString().trim()
            if (surname.isNotBlank() && name.isNotBlank() && father.isNotBlank()){
                val mentor = Mentor(0,course,name,surname,father)
                myDbHelper.insertMentor(mentor)
                findNavController().popBackStack()
            }else
                Snackbar.make(it,"Fill the blank",2000).show()
        }

        binding.toolbarMentorAdd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            AddMentorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}