package uz.frodo.educationsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import uz.frodo.educationsystem.databinding.FragmentAboutBinding
import uz.frodo.educationsystem.db.MyDbHelper


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AboutFragment : Fragment() {
    lateinit var binding: FragmentAboutBinding
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
        binding = FragmentAboutBinding.inflate(layoutInflater)
        val course = MyDbHelper(requireContext()).getCourseById(arguments?.getInt("Course")!!)

        binding.stAdd.setOnClickListener {
            findNavController().navigate(R.id.fullStudentFragment, bundleOf("courseID" to course.id))
        }

        binding.aboutText.text = course.about

        binding.toolbarGroupAdd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            AboutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}