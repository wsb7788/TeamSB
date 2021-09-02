package com.project.teamsb.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.data.remote.login.LoginListener
import com.project.teamsb.data.repository.login.LoginRepository
import com.project.teamsb.post.App
import com.project.teamsb.utils.Coroutines
import kotlinx.coroutines.delay

class LoginViewModel(private val repository: LoginRepository):ViewModel() {
    var loginListener: LoginListener? = null

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
                Log.d("로그","1")
                val loginResponse = repository.login(id, pw)

                if(loginResponse.code == 200){
                    Log.d("로그","1")
                    loginListener?.onLoginSuccess()
                    return@main
                }
                loginListener?.onLoginFailure(loginResponse.message)



            }catch ( e: Exception){
                loginListener?.onLoginFailure(e.message!!)
            }
        }
    }
}