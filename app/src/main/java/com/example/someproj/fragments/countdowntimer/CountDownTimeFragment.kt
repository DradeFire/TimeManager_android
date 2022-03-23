package com.example.someproj.fragments.countdowntimer

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.someproj.MainStartActivity
import com.example.someproj.R
import com.example.someproj.alarm.AlarmReceiver
import com.example.someproj.consts.Const
import com.example.someproj.databinding.FragmentCountDownTimeBinding
import com.example.someproj.viewmodel.DataModel
import java.util.*
import javax.inject.Inject

class CountDownTimeFragment @Inject constructor(): Fragment() {

    private lateinit var binding: FragmentCountDownTimeBinding
    val viewModel: DataModel by viewModels()
    private lateinit var alarmManager: AlarmManager
    private var timer: CountDownTimer? = null
    private var canStart: Boolean = true
    private var timerInMills: Long = 0
    private var isTurn: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountDownTimeBinding.inflate(inflater, container, false)

        if(viewModel.countDown_isTurn != null){
            isTurn = viewModel.countDown_isTurn!!
            timerInMills = viewModel.countDown_timerInMills!!
            canStart = viewModel.countDown_canStart!!
            binding.btStartStop.text = viewModel.countDown_startStopbt
            startCountDownTimer(timerInMills)
        }

        binding.btStartStop.setOnClickListener {

            when(canStart){
                true -> {
                    if(binding.edHourTimer.text.isNotEmpty()
                        and binding.edMinuteTimer.text.isNotEmpty()
                        and binding.edSecondTimer.text.isNotEmpty()) {

                        canStart = false

                        val hour = binding.edHourTimer.text.toString().toInt()
                        val minute = binding.edMinuteTimer.text.toString().toInt()
                        val second = binding.edSecondTimer.text.toString().toInt()
                        timerInMills = (hour * 3600L + minute * 60L + second * 1L) * 1_000L

                        binding.btStartStop.text = getString(R.string.stop)

                        startCountDownTimer(timerInMills)
                    }
                }

                false -> {
                    timer?.cancel()
                    binding.textTimer.visibility = View.GONE
                    binding.linearLayoutTimes.visibility = View.VISIBLE
                    canStart = true

                    binding.btStartStop.text = getString(R.string.start_timer)
                }
            }

        }

        return binding.root
    }

    private fun startCountDownTimer(timeMills: Long) {
        timer?.cancel()

        binding.linearLayoutTimes.visibility = View.GONE
        binding.textTimer.visibility = View.VISIBLE

        timer = object : CountDownTimer(timeMills, 10) {
            override fun onTick(millisUntilFinished: Long) {
                val time = "${dateFormatInt(
                    millisUntilFinished / 3600_000)}:${dateFormatInt(
                    (millisUntilFinished / 60_000) % 60)}:${dateFormatInt(
                    (millisUntilFinished / 1_000) % 60)}.${millisUntilFinished % 10}"
                binding.textTimer.text = time
                timerInMills = millisUntilFinished
            }

            @SuppressLint("UnspecifiedImmutableFlag")
            override fun onFinish() {
                binding.textTimer.visibility = View.GONE
                binding.linearLayoutTimes.visibility = View.VISIBLE
                canStart = true

                viewModel.message.value = getString(R.string.end_timer)
                val calendar = Calendar.getInstance()
                alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmClockInfo =
                    AlarmManager.AlarmClockInfo(
                        calendar.timeInMillis,
                        getAlarmInfoPendingIntent()
                    )
                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent())

                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                intent.putExtra(Const.MESS, viewModel.message.value)

                val pendingIntent = PendingIntent
                    .getBroadcast(
                        requireContext(),
                        Const.REQUEST_CODE_GET_ALARM_INFO,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

                Toast.makeText(requireContext(),
                    context?.getString(R.string.success),
                    Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun dateFormatInt(long: Long): String{
        return when(long) {
            in(0..9) -> "0${long}"
            else -> long.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.countDown_isTurn = true
        viewModel.countDown_canStart = canStart
        viewModel.countDown_timerInMills = timerInMills
        viewModel.countDown_startStopbt = binding.btStartStop.text.toString()
        super.onSaveInstanceState(outState)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun getAlarmInfoPendingIntent(): PendingIntent? {
        val alarmInfoIntent = Intent(requireContext(), MainStartActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            requireContext(),
            Const.REQUEST_CODE_GET_ALARM_INFO,
            alarmInfoIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun getAlarmActionPendingIntent(): PendingIntent? {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            requireContext(),
            Const.REQUEST_CODE_GET_ALARM_ACTION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}