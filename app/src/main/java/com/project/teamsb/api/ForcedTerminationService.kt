package com.project.teamsb.api

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ForcedTerminationService: Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.e("Error","onTaskRemoved - 강제 종료 " + rootIntent);
        stopSelf()
    }
}