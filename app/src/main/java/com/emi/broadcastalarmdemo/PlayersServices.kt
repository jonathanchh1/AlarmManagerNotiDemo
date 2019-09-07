package com.emi.broadcastalarmdemo

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class PlayersServices : Service(){


    private lateinit var player : MediaPlayer
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player =  MediaPlayers(this)
        createNotification(this)
        notificationBuilder(this)
    }


    @TargetApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent!!.action.equals("start") && intent != null){
            Toast.makeText(this, "foreground start", Toast.LENGTH_SHORT).show()
                player.start()
        }else if(intent.action.equals("stop")){
               if(player.isPlaying) {
                   player.stop()
                   player.reset()
                   player.release()
               }

            stopForeground(true)
            stopSelf()
            Toast.makeText(this, "foreground stopped", Toast.LENGTH_SHORT).show()

        }
        return START_STICKY
    }


    private fun MediaPlayers(context: Context) : MediaPlayer{
        return MediaPlayer.create(context, R.raw.song1)
    }

    private fun notificationBuilder(context: Context) : NotificationCompat.Builder{
        val builder = NotificationCompat.Builder(context, AlarmBroadCastReceiver.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_add_alarm_black_24dp)
            .setContentTitle("Sample No Title")
            .setContentText("Sample Notification Text")
            .setStyle(NotificationCompat.BigTextStyle())

        startForeground(1234, builder.build())
        return builder
    }

    private fun createNotification(context : Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "alarm channel"
            val desc = "channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(AlarmBroadCastReceiver.CHANNEL_ID, name, importance)
            channel.description = desc
            val notificationManager  =
                ContextCompat.getSystemService(context, NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }




}