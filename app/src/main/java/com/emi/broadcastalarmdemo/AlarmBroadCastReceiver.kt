package com.emi.broadcastalarmdemo


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startForegroundService

class AlarmBroadCastReceiver  : BroadcastReceiver(){


    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, PlayersServices::class.java)
        serviceIntent.action = "start"
        startForegroundService(context, serviceIntent)
        Toast.makeText(context, "Alarm...", Toast.LENGTH_LONG).show()
    }


    companion object{

        const val CHANNEL_ID = "channelID"
    }
}