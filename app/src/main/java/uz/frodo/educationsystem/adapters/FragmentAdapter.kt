package uz.frodo.educationsystem.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.frodo.educationsystem.InsideFragment

class FragmentAdapter(val list: ArrayList<String>,val courseId:Int,val fr:FragmentActivity):FragmentStateAdapter(fr) {
    var insideFragments = ArrayList<InsideFragment>()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        val insideFragment = InsideFragment.newInstance(list[position],courseId)
        insideFragments.add(insideFragment)
        return insideFragment
    }

    fun getFragments(position: Int):InsideFragment{
        return insideFragments[position]
    }

}