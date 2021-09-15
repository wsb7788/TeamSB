package com.project.teamsb.ui.main

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.teamsb.CalendarFragment
import com.project.teamsb.R
import com.project.teamsb.api.ResultNickname
import com.project.teamsb.api.ResultNotiCheck
import com.project.teamsb.api.ServerObj
import com.project.teamsb.data.remote.main.MainListener
import com.project.teamsb.data.remote.nickname.NicknameListener
import com.project.teamsb.data.repository.login.LoginRepository
import com.project.teamsb.data.repository.main.MainRepository
import com.project.teamsb.data.repository.nickname.NicknameRepository
import com.project.teamsb.main.home.HomeFragment
import com.project.teamsb.main.notice.NoticeFragment
import com.project.teamsb.main.user.UserFragment
import com.project.teamsb.utils.Coroutines
import com.project.teamsb.utils.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MainRepository, private val sharedPreferencesManager: SharedPreferencesManager):ViewModel() {
    var mainListener: MainListener? = null

    var mBackWait:Long = 0

    fun backPressed(){
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            val message = "뒤로가기 버튼을 한번 더 누르면 종료됩니다."
            mainListener?.backPressedMessage(message)
        } else {
            mainListener?.finish()
        }
    }

    fun getUserNickname() {
        Coroutines.main {
            val id = sharedPreferencesManager.getId()
            try {
                val mainResponse = repository.getUserNickname(id)
                if(mainResponse.code == 200){
                    sharedPreferencesManager.saveNickname(mainResponse.content)
                    return@main
                }
                mainListener?.onNicknameFailure(mainResponse.message)
            }catch (e: Exception){
                mainListener?.onNicknameFailure(e.message!!)
            }
        }
    }

    fun checkNotification() {
        Coroutines.main {
            val id = sharedPreferencesManager.getId()
            try {
                val mainResponse = repository.notiCheck(id)
                if (mainResponse.check){
                    if (mainResponse.notificationCount == 0)
                        mainListener?.existNoti()
                    else
                        mainListener?.noExistNoti()
                    return@main
                }
                mainListener?.onNotiFailure(mainResponse.message)
            }catch(e:Exception){
                mainListener?.onNotiFailure(e.message!!)
            }
        }
    }


}