package uz.frodo.educationsystem

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.frodo.educationsystem.databinding.FragmentFullStudentBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.model.Groups
import uz.frodo.educationsystem.model.Mentor
import uz.frodo.educationsystem.model.Student
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FullStudentFragment : Fragment() {
    lateinit var binding: FragmentFullStudentBinding
    lateinit var myDbHelper: MyDbHelper
    lateinit var groupsList:ArrayList<Groups>
    lateinit var mentorList:ArrayList<Mentor>
    val callendar = Calendar.getInstance()
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
        binding = FragmentFullStudentBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(requireContext())
        val courseid = arguments?.getInt("courseID")

        mentorList = myDbHelper.getAllMentors(courseid!!)

        val mList = ArrayList<String>()
        for (i in 0 until mentorList.size) {
            val fullName = mentorList[i].name + " " + mentorList[i].surname
            mList.add(fullName)
        }

        binding.stMentor.adapter = ArrayAdapter(requireContext(), androidx.transition.R.layout.support_simple_spinner_dropdown_item, mList)

        binding.stMentor.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val mentor = mentorList[position]
                groupsList = myDbHelper.getAllGroupsByMentorId(courseid,mentor.id)
                val gList = ArrayList<String>()
                for (i in 0 until groupsList.size){
                    val gName = groupsList[i].name
                    gList.add(gName)
                }
                binding.stGroups.adapter = ArrayAdapter(requireContext(), androidx.transition.R.layout
                    .support_simple_spinner_dropdown_item,gList)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.studentBtnAdd.setOnClickListener {
            val surname = binding.studentSurname.text.toString().trim().trim()
            val name = binding.studentName.text.toString().trim()
            val father = binding.studentFather.text.toString().trim()
            val date = binding.studentDate.text.toString().trim()
            if (surname.isNotBlank() && name.isNotBlank() && father.isNotBlank() && date.isNotBlank() && mentorList.isNotEmpty
                    () && groupsList.isNotEmpty()){
                val m = mentorList[binding.stMentor.selectedItemPosition]
                val g = groupsList[binding.stGroups.selectedItemPosition]

                val student = Student(g,m,name,surname,father,date)
                myDbHelper.insertStudent(student)
                findNavController().popBackStack()
            }else
                Snackbar.make(it,"Fill the blank",2000).show()
        }

        binding.studentDate.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val datePicker = DatePickerDialog(
                    requireContext(), { _, year: Int, month: Int, day: Int ->
                        callendar.set(year, month, day)
                        val formatted = SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(callendar.time)
                        binding.studentDate.setText(formatted)
                    },
                    callendar.get(Calendar.YEAR),
                    callendar.get(Calendar.MONTH),
                    callendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            true // Return 'true' to consume the touch event
        }

        binding.toolbarFullStAdd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            FullStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}