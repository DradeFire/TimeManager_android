package com.example.someproj.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.someproj.R
import com.example.someproj.databinding.ItemListBinding
import com.example.someproj.viewmodel.DataModel
import com.example.someproj.roomdb.model.Task
import javax.inject.Inject

class Adapter @Inject constructor(private val context: Context,
                                  private val datamodel: DataModel)
    : RecyclerView.Adapter<Adapter.ItemViewHolder>() {

    private var taskList = ArrayList<Task>()

    inner class ItemViewHolder(private val binding: ItemListBinding)
        : RecyclerView
        .ViewHolder(binding.root){

        fun bind(task: Task) = with(binding) {

            binding.TitleView.text = task.title
            binding.TaskView.text = task.task
            binding.TimeView.text = task.time

            binding.btDelete.setOnClickListener {
                deleteElement(this@ItemViewHolder.adapterPosition)
            }
        }
    }

    fun deleteElement(itemToRemoveInt: Int) {
        taskList.removeAt(itemToRemoveInt)
        notifyItemRemoved(itemToRemoveInt)
        notifyItemRangeChanged(itemToRemoveInt, itemCount)

        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton(context.getString(R.string.yes)) { _, _ ->
            datamodel.deleteTask(taskList[itemToRemoveInt])
            Toast.makeText(context, context.getString(R.string.success_removed), Toast.LENGTH_SHORT)
                .show()
        }
            .setNegativeButton(context.getString(R.string.no)) { _, _-> }
            .setTitle("Delete ${taskList[itemToRemoveInt].title}")
            .setMessage(context.getString(R.string.are_you_sure))
            .show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = taskList[position]
        holder.bind(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(task: ArrayList<Task>){
        this.taskList = task
        notifyDataSetChanged()
    }

}