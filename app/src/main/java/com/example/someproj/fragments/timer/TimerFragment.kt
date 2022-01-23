package com.example.someproj.fragments.timer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.someproj.R
import com.example.someproj.appComponent
import com.example.someproj.databinding.FragmentTimerBinding
import javax.inject.Inject

class TimerFragment @Inject constructor() : Fragment() {

    lateinit var binding: FragmentTimerBinding
    private var time = 0
    var running: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimerBinding.inflate(inflater, container, false)

        requireContext().appComponent.inject(this)

        return binding.root
    }

    @Inject
    fun startBind(){
        binding.startStopButton.setOnClickListener {

            when(running){
                false -> startT()
                true -> stopT()
            }

        }
        binding.resetButton.setOnClickListener { resetT() }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun startT(){
        running = true
        binding.startStopButton.icon =
            requireActivity().getDrawable(R.drawable.ic_baseline_pause_24)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun stopT(){
        running = false
        binding.startStopButton.icon =
            requireActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resetT(){
        running = false
        time = 0
        binding.startStopButton.icon =
            requireActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24)
    }

    @Inject
    fun runTimer() {
        val handler = Handler()
        handler.post(object : Runnable {

            override fun run() {

                val hour = time / 3600
                val minute = (time % 3600) / 60
                val second = time % 60

                val nowTime = String.format("%d:%02d:%02d", hour, minute, second)
                binding.timeTV.text = nowTime

                if (running){
                    time++
                }

                handler.postDelayed(this, 1000)
            }
        })
    }

}