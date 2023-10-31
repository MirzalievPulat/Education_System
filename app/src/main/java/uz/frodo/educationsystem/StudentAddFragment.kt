package uz.frodo.educationsystem

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.frodo.educationsystem.databinding.FragmentStudentAddBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.model.Student
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StudentAddFragment : Fragment() {
    lateinit var binding: FragmentStudentAddBinding
    lateinit var myDbHelper: MyDbHelper
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
        binding = FragmentStudentAddBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(requireContext())
        val group = myDbHelper.getGroupById(arguments?.getInt("Group_id")!!)
        val mentor = myDbHelper.getMentorById(group.mentor.id)

        binding.studentBtnAdd.setOnClickListener {
            val surname = binding.studentSurname.text.toString().trim()
            val name = binding.studentName.text.toString().trim()
            val father = binding.studentFather.text.toString().trim()
            val date = binding.studentDate.text.toString().trim()
            if (surname.isNotBlank() && name.isNotBlank() && father.isNotBlank() && date.isNotBlank()){
                val student = Student(group,mentor,name,surname,father,date)
                myDbHelper.insertStudent(student)
                findNavController().popBackStack()
            }else
                Snackbar.make(it,"Fill the blank",2000).show()
        }


        binding.studentDate.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val datePicker = DatePickerDialog(
                    requireContext(), {_, year: Int, month: Int, day: Int->
                        callendar.set(year,month,day)
                        val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(callendar.time)
                        binding.studentDate.setText(formatted)
                    },
                    callendar.get(Calendar.YEAR),
                    callendar.get(Calendar.MONTH),
                    callendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            true // Return 'true' to consume the touch event
        }

        binding.toolbarStudentAdd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            StudentAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}