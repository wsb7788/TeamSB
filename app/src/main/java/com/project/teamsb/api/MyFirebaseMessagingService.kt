package com.project.teamsb.api

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.project.teamsb.R
import com.project.teamsb.login.LoginActivity
import com.project.teamsb.main.MainActivity
import com.project.teamsb.toolbar.AlertActivity

class MyFirebaseMessagingService:FirebaseMessagingService() {
    private val TAG: String = this.javaClass.simpleName
    lateinit var managerCompat: NotificationManagerCompat

    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {


        if(remoteMessage.data.isNotEmpty()){
            sendNotification(remoteMessage.data!!["title"]!!, remoteMessage.data["body"]!!)

        }
        if (remoteMessage.notification != null)
        {

            sendNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    override fun onNewToken(token: String)
    {
        Log.d(TAG, "Refreshed token : $token")
        super.onNewToken(token)
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()
    }

    // 받은 알림을 기기에 표시하는 메서드
    @SuppressLint("ServiceCast")
    private fun sendNotification(title:String, body:String)
    {

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0  , intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = "my_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        try{
        val notificationBuilder = NotificationCompat.Builder(this,channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.logo)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        //val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManagerCompat
        val notificationManager = NotificationManagerCompat.from(this)
        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

        }
        catch(e: Exception){
            e.printStackTrace();
        }
    }


}