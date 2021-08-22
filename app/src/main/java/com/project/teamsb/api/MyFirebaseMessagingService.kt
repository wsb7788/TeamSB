package com.project.teamsb.api

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseCommonRegistrar
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingRegistrar
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.FirebaseMessagingKtxRegistrar
import com.google.firebase.messaging.ktx.messaging
import com.project.teamsb.R
import com.project.teamsb.login.LoginActivity
import com.project.teamsb.login.SplashActivity
import com.project.teamsb.main.MainActivity
import com.project.teamsb.post.App
import java.util.*

class MyFirebaseMessagingService:FirebaseMessagingService() {
    private val TAG: String = this.javaClass.simpleName
    lateinit var managerCompat: NotificationManagerCompat

    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {


        if(remoteMessage.data.isNotEmpty()){
            sendNotification(remoteMessage.data!!["title"]!!, remoteMessage.data["body"]!!)

        }
        if (remoteMessage.notification == null)
        {
            //sendNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
            //sendNotification(remoteMessage.data!!["title"]!!, remoteMessage.data["body"]!!)
        }
        sendNotification(remoteMessage.data!!["title"]!!, remoteMessage.data["body"]!!)
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
        val random = (System.currentTimeMillis()/1000).toInt()
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, random  , intent,
            PendingIntent.FLAG_ONE_SHOT)


        val channelId = "댓글 및 공지사항"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val micon = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.logo)
        try{




            val notificationBuilder = NotificationCompat.Builder(App.instance, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(micon)
                .setColor(Color.parseColor("#ff0000"))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent,true)                //위에 띄우기
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)            //잠금화면에서 보이기
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)


            val name = "댓글 및 공지사항"
            val descriptionText = "댓글 및 공지사항 알림입니다."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }


            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager      //api 26부터
            val channelId = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)


        //val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManagerCompat


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                // 오레오 버전 예외처리

            }
        notificationManager.notify(random /* ID of notification */, notificationBuilder.build())

        }
        catch(e: Exception){
            e.printStackTrace();
        }
    }


}