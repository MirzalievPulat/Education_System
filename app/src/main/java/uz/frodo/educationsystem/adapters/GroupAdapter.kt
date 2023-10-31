package uz.frodo.educationsystem.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.frodo.educationsystem.databinding.GroupItemBinding
import uz.frodo.educationsystem.db.MyDbHelper
import uz.frodo.educationsystem.interfaces.OnClickGroup
import uz.frodo.educationsystem.model.Groups


class GroupAdapter(val onClickGroup: OnClickGroup,val myDbHelper: MyDbHelper):ListAdapter<Groups,GroupAdapter.VH>(GroupDiffUtil()) {
    inner class VH(val binding: GroupItemBinding):ViewHolder(binding.root)

    class GroupDiffUtil(): DiffUtil.ItemCallback<Groups>() {
        override fun areItemsTheSame(oldItem: Groups, newItem: Groups): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Groups, newItem: Groups): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = GroupItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.gIName.text = getItem(position).name
        holder.binding.gIAmount.text = myDbHelper.getStudentByGroupId(getItem(position).id).size.toString()
        holder.binding.gIView.setOnClickListener {
            onClickGroup.onView(getItem(position))
        }
        holder.binding.gIEdit.setOnClickListener {
            onClickGroup.onEdit(getItem(position),position)
        }
        holder.binding.gIDelete.setOnClickListener {
            Log.d("@@@", "onBindViewHolder: $position")
            onClickGroup.onDelete(getItem(position))
        }
    }


}