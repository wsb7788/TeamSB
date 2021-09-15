package com.project.teamsb.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.project.teamsb.R
import com.project.teamsb.data.remote.login.LoginListener
import com.project.teamsb.data.repository.login.LoginRepository
import com.project.teamsb.post.App
import com.project.teamsb.utils.Coroutines
import com.project.teamsb.utils.SharedPreferencesManager
import kotlinx.coroutines.delay

class LoginViewModel(private val repository: LoginRepository, private val sharedPreferencesManager: SharedPreferencesManager):ViewModel() {
    var loginListener: LoginListener? = null

    var mBackWait:Long = 0
    val id: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            postValue("")
        }
    }
    val pw: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            postValue("")
        }
    }

    fun login(){
        val id = id.value.toString()
        val pw = pw.value.toString()
        loginListener?.onLoginStarted()
        Coroutines.main {
            try {
                val loginResponse = repository.login(id, pw)
                if(loginResponse.code == 200){
                    sharedPreferencesManager.saveUser(id)
                    if(loginResponse.nickname){
                        loginListener?.onStartMain()
                        return@main
                    }
                   loginListener?.onStartNickname()
                    return@main
                }
                loginListener?.onLoginFailure(loginResponse.message)
            }catch ( e: Exception){
                loginListener?.onLoginFailure(e.message!!)
            }
        }
    }

    fun backPressed() {
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            val message = "뒤로가기 버튼을 한번 더 누르면 종료됩니다."
            loginListener?.backPressedMessage(message)
        } else {
            loginListener?.finish()
        }
    }
}