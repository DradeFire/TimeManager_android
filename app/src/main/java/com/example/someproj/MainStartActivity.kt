package com.example.someproj

import android.annotation.SuppressLint
import android.app.*
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.someproj.di.App
import com.example.someproj.di.AppComponent
import com.example.someproj.alarm.AlarmReceiver
import com.example.someproj.consts.Const
import com.example.someproj.databinding.ActivityMainBinding
import com.example.someproj.viewmodel.DataModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val Context.appComponent: AppComponent
    get() = when(this){
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

@AndroidEntryPoint
class MainStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    val dataModel: DataModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appComponent.inject(this@MainStartActivity)

    }

    @Inject
    fun setupFragment() {
        navController = (supportFragmentManager.findFragmentById(R.id.fragment)
                as NavHostFragment).navController
        setupActionBarWithNavController(navController)
    }

    @Inject
    fun navigationChooseFragment() {
        binding.nv.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notifTimeManager -> {

                    changeFragment(R.id.timeManagerFragment)
                }
                R.id.notifManager -> {

                    changeFragment(R.id.listFragment)
                }
                R.id.notifTimer -> {

                    changeFragment(R.id.countDownTimerFragment)
                }
                R.id.notifSeconder -> {

                    changeFragment(R.id.timerFragment)
                }
            }

            true
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun changeFragment(action: Int) {

        navController.navigate(action)
        binding
            .drawerLayout
            .closeDrawer(Gravity.LEFT, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    fun getAlarmInfoPendingIntent(): PendingIntent? {
        val alarmInfoIntent = Intent(this, MainStartActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            this,
            Const.REQUEST_CODE_GET_ALARM_INFO,
            alarmInfoIntent,
            FLAG_UPDATE_CURRENT
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun getAlarmActionPendingIntent(): PendingIntent? {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            this,
            Const.REQUEST_CODE_GET_ALARM_ACTION,
            intent,
            FLAG_UPDATE_CURRENT)
    }

    @Inject
    @SuppressLint("UnspecifiedImmutableFlag")
    fun calendarObserve() {
        dataModel.calendar.observe(this) {
            val calendar = dataModel.calendar.value!!
            val alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val alarmClockInfo =
                AlarmClockInfo(calendar.timeInMillis, getAlarmInfoPendingIntent())
            alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent())

            val intent = Intent(this@MainStartActivity, AlarmReceiver::class.java)
            intent.putExtra(Const.MESS, dataModel.message.value)

            val pendingIntent = PendingIntent
                .getBroadcast(
                    this@MainStartActivity,
                    Const.REQUEST_CODE_GET_ALARM_INFO,
                    intent,
                    FLAG_UPDATE_CURRENT
                )

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

            Toast.makeText(
                this@MainStartActivity,
                getString(R.string.success),
                Toast.LENGTH_SHORT
            ).show()

        }

    }

}