package uz.frodo.educationsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import uz.frodo.educationsystem.adapters.StudentAdapter
import uz.frodo.educationsystem.databinding.FragmentGroupInsideBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.interfaces.OnClickStudent
import uz.frodo.educationsystem.model.Groups
import uz.frodo.educationsystem.model.Student


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class GroupInsideFragment : Fragment(),OnClickStudent {
    lateinit var binding: FragmentGroupInsideBinding
    lateinit var myDbHelper: MyDbHelper
    lateinit var adapter: StudentAdapter
    lateinit var group: Groups
    lateinit var mainList: ArrayList<Student>
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
        binding = FragmentGroupInsideBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(requireContext())
        group = myDbHelper.getGroupById(arguments?.getInt("group_id")!!)


        binding.toolbarInsideGroup.title = group.name
        binding.gName.text = group.name
        val fullMentor = group.mentor.name+" "+group.mentor.surname
        binding.gMentor.text = fullMentor
        binding.gAmount.text = myDbHelper.getStudentByGroupId(group.id).size.toString()
        binding.gDays.text = group.day
        binding.gHours.text = group.hours
        if (group.started == 1)  binding.startLesson.visibility = View.GONE

        adapter = StudentAdapter(this)
        mainList = myDbHelper.getAllStudents(group.id)
        adapter.submitList(mainList)
        binding.rvStudent.adapter = adapter


        binding.startLesson.setOnClickListener {
            val gr = Groups(group.id,group.course,group.mentor,group.name,group.day,group.hours,1)
            myDbHelper.updateGroup(gr)
            InsideFragment.updateData.invoke()
            findNavController().popBackStack()
        }

        binding.studentAdd.setOnClickListener {
            findNavController().navigate(R.id.studentAddFragment, bundleOf("Group_id" to group.id))
        }

        binding.toolbarInsideGroup.setNavigationOnClickListener {
            findNavController().popBackStack()

        }

        return binding.root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            GroupInsideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun editStudent(student: Student) {
        findNavController().navigate(R.id.studentEditFragment, bundleOf("student_id" to student.id))
    }

    override fun deleteStudent(student: Student) {
        myDbHelper.deleteStudent(student)
        mainList.remove(student)
        adapter.submitList(mainList)
        binding.rvStudent.adapter = adapter

        binding.gAmount.text = myDbHelper.getStudentByGroupId(group.id).size.toString()
    }
}