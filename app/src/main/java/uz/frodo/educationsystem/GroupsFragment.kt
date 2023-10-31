package uz.frodo.educationsystem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import uz.frodo.educationsystem.adapters.FragmentAdapter
import uz.frodo.educationsystem.databinding.FragmentGroupsBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.model.Course


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class GroupsFragment : Fragment() {
    lateinit var binding: FragmentGroupsBinding
    lateinit var myDbHelper: MyDbHelper
    lateinit var course: Course
    lateinit var adapter: FragmentAdapter
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

        binding = FragmentGroupsBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(requireContext())

        course = myDbHelper.getCourseById(arguments?.getInt("Group")!!)
        binding.toolbarGroups.title = course.name

        val list = arrayListOf("Ochilgan guruxlar", "Ochilayotgan guruxlar")

        adapter = FragmentAdapter(list, course.id, requireActivity())
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Ochilgan guruxlar"
                }

                1 -> {
                    tab.text = "Ochilayotgan guruxlar"
                }
            }
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {

                        binding.groupAdd.visibility = View.INVISIBLE
                    }

                    1 -> {

                        binding.groupAdd.visibility = View.VISIBLE
                    }
                }
            }
        })


        binding.groupAdd.setOnClickListener {
            findNavController().navigate(R.id.addGroupFragment, bundleOf("CourseID" to course.id))
        }

        binding.toolbarGroups.setNavigationOnClickListener {
            findNavController().popBackStack()
        }



        return binding.root
    }

    companion object {


        fun newInstance(param1: String, param2: String) =
            GroupsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}