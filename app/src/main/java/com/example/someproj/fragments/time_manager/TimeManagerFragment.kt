package com.example.someproj.fragments.time_manager

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.someproj.di.App
import com.example.someproj.di.AppComponent
import com.example.someproj.databinding.FragmentTimeManagerBinding
import com.example.someproj.datamodel.DataModel
import java.util.*
import javax.inject.Inject

val Context.appComponent: AppComponent
    get() = when(this){
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

class TimeManagerFragment @Inject constructor(): Fragment() {

    private lateinit var binding: FragmentTimeManagerBinding
    @Inject
    lateinit var date: Calendar
    val dataModel: DataModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentTimeManagerBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding

        requireContext().appComponent.inject(this)

        binding.edYear.setText(date.get(Calendar.YEAR).toString())
        binding.edMonth.setText((date.get(Calendar.MONTH) + 1).toString())
        binding.edDay.setText(date.get(Calendar.DAY_OF_MONTH).toString())

        return binding.root
    }

    @Inject
    fun startObs(){

        binding.bSetNotify.setOnClickListener {

            binding.apply {

                val hour = edHour.text.toString().toInt()
                val minute = edMinute.text.toString().toInt()
                val message = edMessage.text.toString()
                val day = edDay.text.toString().toInt()
                val month = edMonth.text.toString().toInt()
                val year = edYear.text.toString().toInt()

                date = Calendar.getInstance()
                date[Calendar.SECOND] = 0
                date[Calendar.MILLISECOND] = 0
                date[Calendar.MINUTE] = minute
                date[Calendar.HOUR_OF_DAY] = hour
                date[Calendar.DAY_OF_MONTH] = day
                date[Calendar.MONTH] = month
                date[Calendar.YEAR] = year

                dataModel.message.value = message
                dataModel.calendar.value = date
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = TimeManagerFragment()
    }
}