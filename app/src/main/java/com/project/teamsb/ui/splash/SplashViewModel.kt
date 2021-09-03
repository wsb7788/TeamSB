package com.project.teamsb.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.data.remote.login.LoginListener
import com.project.teamsb.data.remote.splash.SplashListener
import com.project.teamsb.data.repository.login.LoginRepository
import com.project.teamsb.post.App
import com.project.teamsb.utils.Coroutines
import com.project.teamsb.utils.SharedPreferencesManager
import kotlinx.coroutines.delay

class SplashViewModel(private val sharedPreferencesManager: SharedPreferencesManager):ViewModel() {
    var splashListener:SplashListener? = null



    fun checkAutoLogin(){

        val autoLoginSuccess = sharedPreferencesManager.getAutoLoginSuccess()

        if(autoLoginSuccess){
            splashListener?.onStartMain()
            return
        }
        splashListener?.onStartLogin()

    }



}