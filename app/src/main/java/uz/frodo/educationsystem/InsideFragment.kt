package uz.frodo.educationsystem

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import uz.frodo.educationsystem.adapters.GroupAdapter
import uz.frodo.educationsystem.databinding.FragmentInsideBinding
import uz.frodo.educationsystem.databinding.GrpupDialogBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.interfaces.OnClickGroup
import uz.frodo.educationsystem.model.Groups


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class InsideFragment : Fragment(),OnClickGroup {
    lateinit var binding: FragmentInsideBinding
    lateinit var adapter: GroupAdapter
    lateinit var myDbHelper: MyDbHelper
    lateinit var mainList:ArrayList<Groups>
    var started:Int? = null
    private var param1: String? = null
    private var param2: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("Inside Fragment")
        binding = FragmentInsideBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(requireContext())
        when(param1){
            "Ochilgan guruxlar" -> {
                mainList = myDbHelper.getAllGroups(param2!!,1)
                started = 1
            }
            "Ochilayotgan guruxlar" ->{
                mainList = myDbHelper.getAllGroups(param2!!,0)
                started = 0
            }
        }

        adapter = GroupAdapter(this,myDbHelper)
        adapter.submitList(mainList)
        binding.rvGroups.adapter = adapter

        updateData={
            updateAdapter()
        }

        return binding.root
    }
    fun updateAdapter(){
        mainList.clear()
        mainList = myDbHelper.getAllGroups(param2!!,started!!)
        adapter.submitList(mainList)
        binding.rvGroups.adapter = adapter
    }
    companion object {

        var updateData:()->Unit ={

        }

        fun newInstance(param1: String, param2: Int) =
            InsideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2,param2)
                }
            }
    }

    override fun onView(groups: Groups) {
        findNavController().navigate(R.id.groupInsideFragment, bundleOf("group_id" to groups.id))
    }

    override fun onEdit(groups: Groups,position:Int) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val gBinding = GrpupDialogBinding.inflate(layoutInflater)
        dialog.setView(gBinding.root)
        gBinding.gDialogE1.setText(groups.name)

        val list = myDbHelper.getAllMentors(param2!!)
        val namesList = ArrayList<String>()
        for (i in 0 until list.size){
            val names = list[i].name +" "+ list[i].surname
            namesList.add(names)
        }
        gBinding.gDialogMentor.adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,namesList)
        dialog.setCancelable(false)

        gBinding.mDialogP.setOnClickListener {
            val name = gBinding.gDialogE1.text.toString().trim()
            val mentor = list[gBinding.gDialogMentor.selectedItemPosition]
            val day = gBinding.gDialogDay.selectedItem.toString()
            val hours = gBinding.gDialogHours.selectedItem.toString()
            if (name.isNotBlank()){
                val g = Groups(groups.id,groups.course,mentor,name,day,hours,started!!)
                myDbHelper.updateGroup(g)
                mainList[position] = g
                adapter.submitList(mainList)
                binding.rvGroups.adapter =adapter
                dialog.dismiss()
            }else
                Snackbar.make(it,"Fill the blank",2000).show()

        }
        gBinding.mDialogN.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onDelete(groups: Groups) {
        Log.d("@@@", "onDelete: $groups")
        myDbHelper.deleteGroup(groups)
        mainList.remove(groups)
        adapter.submitList(mainList)
        binding.rvGroups.adapter = adapter
    }
}