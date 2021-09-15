package com.project.teamsb.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class SharedPreferencesManager(private val context: Context){

    companion object{
        const val TAG = "MOBILE_TEMPLATE_APP"
        const val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"
        const val ERROR_TAG = "WEKIT_ERROR_TAG"
        const val CHECK_TAG = "WEKIT_CHECK_TAG"
        const val CLIENT_ID = "CLIENT_ID"
        const val NICKNAME = "NICKNAME"
        const val PUSH_FLAG = "PUSH_FLAG"
        const val BODY = "BODY"
        const val USER = "USER"
        const val EMAIL = "EMAIL"
    }


    private fun getUserInfoPref() : SharedPreferences {
        return context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
    }
    private fun getSettingInfoPref() : SharedPreferences{
        return context.getSharedPreferences("SettingInfo",Context.MODE_PRIVATE)
    }
    private fun getTokenPref() : SharedPreferences{
        return context.getSharedPreferences("token",Context.MODE_PRIVATE)
    }

    private fun getToken():String{
        val pref = getTokenPref()
        return pref.getString("token","")!!
    }
    fun removeAll(){
        val pref = getUserInfoPref()
        val edit = pref.edit()
        edit.clear()
        edit.apply()
    }

    fun saveUser(id: String){
        val pref = getUserInfoPref()
        val edit = pref.edit()
        edit.putString("id",id)
        edit.putString("token",getToken())
        edit.apply()
    }

    fun notFirstLaunch(){
        val pref = getSettingInfoPref()
        val edit = pref.edit()
        edit.putBoolean("isFirstLaunch",false)
        edit.apply()
    }
    fun saveAutoLogin(){
        val pref = getUserInfoPref()
        val edit = pref.edit()
        edit.putBoolean("autoLoginSuccess",true)
        edit.apply()
    }
    fun getId():String{
        val pref = getUserInfoPref()
        return pref.getString("id","")!!
    }

    fun saveNickname(nickname: String) {
        val pref = getUserInfoPref()
        val edit = pref.edit()
        edit.putBoolean("nickname",true)
        edit.apply()
    }

    fun getAutoLoginSuccess(): Boolean {
        val pref = getUserInfoPref()
        return pref.getBoolean("autoLoginSuccess",false)
    }


}