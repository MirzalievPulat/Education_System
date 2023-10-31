package uz.frodo.educationsystem.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.frodo.educationsystem.R
import uz.frodo.educationsystem.databinding.MentorItemBinding
import uz.frodo.educationsystem.interfaces.OnClickMentor
import uz.frodo.educationsystem.model.Mentor

class MentorAdapter(val onClickMentor: OnClickMentor):ListAdapter<Mentor, MentorAdapter.VH>(MyDifUtil()) {
    inner class VH(view:View):ViewHolder(view)

    class MyDifUtil():DiffUtil.ItemCallback<Mentor>(){
        override fun areItemsTheSame(oldItem: Mentor, newItem: Mentor): Boolean {
            return oldItem.id.equals(newItem.id)
        }

        override fun areContentsTheSame(oldItem: Mentor, newItem: Mentor): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mentor_item,parent,false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val nAndS = getItem(position).surname +" "+ getItem(position).name
        holder.itemView.findViewById<TextView>(R.id.m_i_surname).text = nAndS
        holder.itemView.findViewById<TextView>(R.id.m_i_father).text = getItem(position).father
        holder.itemView.findViewById<View>(R.id.m_i_edit).setOnClickListener {
            onClickMentor.onEditClick(getItem(position),position)
        }
        holder.itemView.findViewById<View>(R.id.m_i_delete).setOnClickListener {
            onClickMentor.onDeleteClick(getItem(position))
        }
    }
}