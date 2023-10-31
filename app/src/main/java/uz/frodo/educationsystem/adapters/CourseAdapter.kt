package uz.frodo.educationsystem.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.frodo.educationsystem.databinding.CourseItemBinding
import uz.frodo.educationsystem.model.Course

class CourseAdapter(val courseClick: CourseClick):ListAdapter<Course,CourseAdapter.VH>(MydiffUtil()) {

    inner class VH(val binding: CourseItemBinding):ViewHolder(binding.root)

    class MydiffUtil:DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val biding = CourseItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(biding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.courseItemText.text = getItem(position).name
        holder.binding.courseItemCard.setOnClickListener {
            courseClick.itemClick(getItem(position))
        }
    }

    interface CourseClick{
        fun itemClick(course: Course)
    }
}