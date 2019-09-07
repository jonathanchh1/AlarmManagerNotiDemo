package com.emi.broadcastalarmdemo

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ServiceCompat.stopForeground


import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var alarmManager: AlarmManager?=null
    private var hour : Int?=null
    private var mMinute : Int?=null
    private var min : Int?=null
    private var mHour : Int?=null
    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start.setOnClickListener {
            startAlert()
            alarmsetInfo.text ="Alarm have been : Set"
        }

        stop.setOnClickListener {
            if (alarmManager == null) {
                Toast.makeText(applicationContext, "please set your alarm", Toast.LENGTH_SHORT).show()
            }else{
                alarmManager!!.cancel(pendingIntent)
                alarmsetInfo.text = "Alarm have been : Stopped"
                val intent = Intent(this, PlayersServices::class.java)
                intent.action = "stop"
                startForegroundService(intent)
            }
        }

        button_time.setOnClickListener {
            val calendar = Calendar.getInstance()
            mHour = calendar.get(Calendar.HOUR_OF_DAY)
            mMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog  = TimePickerDialog(this,
                TimePickerDialog.OnTimeSetListener {_, hourOfday, minute ->
                    val s = "$hourOfday : $minute"
                    time.text = s
                    hour = hourOfday
                    min = minute
                }, mHour!!, mMinute!!, false)
            timePickerDialog.show()
         }


    }




   private val pendingIntent : PendingIntent
    get() {
        val intent = Intent(this, AlarmBroadCastReceiver::class.java)
        return PendingIntent.getBroadcast(
            applicationContext,
            100, intent, 0)
    }


    private fun startAlert(){
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour!!)
        calendar.set(Calendar.MINUTE, min!!)
        alarmManager!!.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, pendingIntent)
    }
}
