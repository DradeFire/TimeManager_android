package com.example.someproj.fragments.task_manager

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.someproj.R
import com.example.someproj.databinding.FragmentSetTaskBinding
import com.example.someproj.viewmodel.DataModel
import com.example.someproj.roomdb.model.Task
import java.util.*
import javax.inject.Inject

class SetTaskFragment @Inject constructor(): Fragment() {

    private lateinit var binding: FragmentSetTaskBinding
    val dataModel: DataModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetTaskBinding.inflate(inflater, container, false)

        binding.btSetTaskNow.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }

    private fun insertDataToDatabase(){

        val title = binding.edSetTaskTitle.text.toString()
        val task = binding.edSetTaskText.text.toString()
        val time = takeTime()

        if(inputCheck(title, task)){
            dataModel.addTask(Task(0, title, task, time))
            Toast.makeText(requireContext(),
                context?.getString(R.string.success),
                Toast.LENGTH_LONG)
                .show()
            findNavController().navigate(R.id.action_setTaskFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(),
                context?.getString(R.string.fill_out_all_fields),
                Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun takeTime(): String{
        val date = Calendar.getInstance()

        val hour = dateFormatInt(date.get(Calendar.HOUR_OF_DAY))
        val minute = dateFormatInt(date.get(Calendar.MINUTE))
        val day = dateFormatInt(date.get(Calendar.DAY_OF_MONTH))
        val month = dateFormatInt(Calendar.MONTH)
        val year = dateFormatInt(date.get(Calendar.YEAR))

        return hour.plus(":")
            .plus(minute).plus("  ")
            .plus(day).plus(".")
            .plus(month).plus(".")
            .plus(year)
    }

    private fun dateFormatInt(int: Int): String{
        return when(int) {
            in(0..9) -> "0${int}"
            else -> int.toString()
        }
    }

    private fun inputCheck(title: String, task: String): Boolean{
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(task))
    }

    companion object {

        @JvmStatic
        fun newInstance() = SetTaskFragment()
    }
}