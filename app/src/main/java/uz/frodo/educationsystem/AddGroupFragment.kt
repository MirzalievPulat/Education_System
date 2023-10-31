package uz.frodo.educationsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.frodo.educationsystem.databinding.FragmentAddGroupBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.model.Groups
import uz.frodo.educationsystem.model.Mentor


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddGroupFragment : Fragment() {
    lateinit var binding: FragmentAddGroupBinding
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
        binding = FragmentAddGroupBinding.inflate(layoutInflater)
        val myDbHelper = MyDbHelper(requireContext())
        val courseId = arguments?.getInt("CourseID")

        val list = myDbHelper.getAllMentors(courseId!!)
        val namesList = ArrayList<String>()
        for (i in 0 until list.size){
            val names = list[i].name +" "+ list[i].surname
            namesList.add(names)
        }

        binding.groupMentor.adapter = ArrayAdapter(requireContext(),
            androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,namesList)

        binding.buttonSave.setOnClickListener {
            val name = binding.groupSurname.text.toString().trim()

            if (name.isNotBlank() && namesList.isNotEmpty() ){
                val course = myDbHelper.getCourseById(courseId)
                val mentor = list[binding.groupMentor.selectedItemPosition]
                val day = binding.groupDays.selectedItem.toString()
                val hours = binding.groupHours.selectedItem.toString()

                val groups = Groups(0,course,mentor,name,day,hours,0)
                myDbHelper.insertGroup(groups)
                InsideFragment.updateData.invoke()
                findNavController().popBackStack()
            }else
                Snackbar.make(it,"Fill the Blank",2000).show()
        }

        binding.toolbarGroupAdd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            AddGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}