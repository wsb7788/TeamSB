package com.project.teamsb.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.teamsb.data.remote.nickname.NicknameListener
import com.project.teamsb.data.repository.nickname.NicknameRepository
import com.project.teamsb.utils.Coroutines
import com.project.teamsb.utils.SharedPreferencesManager

class MainViewModel(private val repository: NicknameRepository, private val sharedPreferencesManager: SharedPreferencesManager):ViewModel() {
    var nicknameListener: NicknameListener? = null

    val nickname: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            postValue("")
        }
    }
    fun nicknameCheck(){
        Coroutines.main {
            try{
                val nicknameResponse = repository.nicknameCheck(nickname.value.toString())
                nicknameListener?.onNicknameCheckResponse(nicknameResponse.message)
            }catch (e:Exception){
                nicknameListener?.onNicknameSetFailure(e.message!!)
            }
        }
    }
    fun nicknameSet(){
        val nickname = nickname.value.toString()
        Coroutines.main {
            try {
                val id = sharedPreferencesManager.getId()
                val nicknameResponse = repository.nicknameSet(id,nickname)
                if(nicknameResponse.code == 200){
                    sharedPreferencesManager.saveNickname(nickname)
                    nicknameListener?.onNicknameSetSuccess()
                }
                nicknameListener?.onNicknameSetFailure(nicknameResponse.message)
            }catch (e: Exception){
                nicknameListener?.onNicknameSetFailure(e.message!!)
            }
        }
    }


    /*fun login(){

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
    }*/
}