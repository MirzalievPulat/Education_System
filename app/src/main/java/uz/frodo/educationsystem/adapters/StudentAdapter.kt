package uz.frodo.educationsystem.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.frodo.educationsystem.databinding.StudentItemBinding
import uz.frodo.educationsystem.interfaces.OnClickStudent
import uz.frodo.educationsystem.model.Student

class StudentAdapter(val onClickStudent: OnClickStudent):ListAdapter<Student,StudentAdapter.SVH>(SDiffUtil()) {
    class SVH(val binding: StudentItemBinding):ViewHolder(binding.root)

    class SDiffUtil():DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SVH {
        val binding = StudentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SVH(binding)
    }

    override fun onBindViewHolder(holder: SVH, position: Int) {
        val sur = getItem(position).surname + " " + getItem(position).name
        holder.binding.sISurname.text = sur
        holder.binding.sIFather.text = getItem(position).father
        holder.binding.sIDate.text = getItem(position).date
        holder.binding.sIEdit.setOnClickListener {
            onClickStudent.editStudent(getItem(position))
        }
        holder.binding.sIDelete.setOnClickListener {
            onClickStudent.deleteStudent(getItem(position))
        }
    }
}