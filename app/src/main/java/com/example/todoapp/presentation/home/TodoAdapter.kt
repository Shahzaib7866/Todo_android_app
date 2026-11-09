
package com.example.todoapp.presentation.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTodoBinding
import com.example.todoapp.domain.model.Todo

class TodoAdapter(
    private val onToggle: (Todo) -> Unit,
    private val onClick: (Todo) -> Unit,
    private val onDelete: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallback) {

    inner class TodoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.txtTaskTitle.text = todo.title

            binding.cbTask.setOnCheckedChangeListener(null)
            binding.cbTask.isChecked = todo.isCompleted

            binding.txtTaskTitle.paintFlags = if (todo.isCompleted) {
                binding.txtTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.txtTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            binding.cbTask.setOnCheckedChangeListener { _, _ -> onToggle(todo) }
            binding.root.setOnClickListener { onClick(todo) }
            binding.btnDelete.setOnClickListener { onDelete(todo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(old: Todo, new: Todo) = old.id == new.id
        override fun areContentsTheSame(old: Todo, new: Todo) = old == new
    }

}