package uz.frodo.educationsystem

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.frodo.educationsystem.databinding.FragmentStudentEditBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.model.Student
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class StudentEditFragment : Fragment() {
    lateinit var binding: FragmentStudentEditBinding
    var callendar = Calendar.getInstance()
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
        binding = FragmentStudentEditBinding.inflate(layoutInflater)
        val myDbHelper = MyDbHelper(requireContext())
        val student = myDbHelper.getStudentById(arguments?.getInt("student_id")!!)

        binding.editStudentSurname.setText(student.surname)
        binding.editStudentName.setText(student.name)
        binding.editStudentFather.setText(student.father)
        binding.editStudentDate.setText(student.date)

        binding.studentBtnEdit.setOnClickListener {
            val surname = binding.editStudentSurname.text.toString().trim()
            val name = binding.editStudentName.text.toString().trim()
            val father = binding.editStudentFather.text.toString().trim()
            val date = binding.editStudentDate.text.toString().trim()
            if (surname.isNotBlank() && name.isNotBlank() && father.isNotBlank() && date.isNotBlank()){
                val s = Student(student.id!!,student.groups,student.mentor,name,surname,father,date)
                myDbHelper.updateStudent(s)
                findNavController().popBackStack()
            }else
                Snackbar.make(it,"Fill the blank",2000).show()
        }

        binding.editStudentDate.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val datePicker = DatePickerDialog(
                    requireContext(), {_, year: Int, month: Int, day: Int->
                        callendar.set(year,month,day)
                        val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(callendar.time)
                        binding.editStudentDate.setText(formatted)
                    },
                    callendar.get(Calendar.YEAR),
                    callendar.get(Calendar.MONTH),
                    callendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            true // Return 'true' to consume the touch event
        }

        binding.toolbarStudentEdit.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            StudentEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}