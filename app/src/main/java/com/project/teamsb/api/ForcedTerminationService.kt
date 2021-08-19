package com.project.teamsb.api

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.project.teamsb.main.calendar.CalendarObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForcedTerminationService: Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.e("Error","onTaskRemoved - 강제 종료 " + rootIntent);
        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        if(!pref.getBoolean("autoLoginSuccess",false)){
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val edit = pref.edit()
                val id = pref.getString("id","")!!
                if(!pref.getBoolean("autoLoginSuccess",false)){
                    try {
                        CalendarObj.api.getToken(id,null).enqueue(object : Callback<ResultNoReturn> {
                            override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                            }
                            override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                                if (response.body()!!.check) {
                                    edit.clear()
                                    edit.commit()
                                }
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

        stopSelf()
    }

    private fun deleteAll() {

    }
}