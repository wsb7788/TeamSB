package com.project.teamsb.ui.home

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.teamsb.CalendarFragment
import com.project.teamsb.R
import com.project.teamsb.api.ResultNickname
import com.project.teamsb.api.ResultNotiCheck
import com.project.teamsb.api.ServerObj
import com.project.teamsb.data.remote.home.HomeListener
import com.project.teamsb.data.remote.main.MainListener
import com.project.teamsb.data.remote.nickname.NicknameListener
import com.project.teamsb.data.repository.home.HomeRepository
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

class HomeViewModel(private val repository: HomeRepository):ViewModel() {
    var homeListener: HomeListener? = null





}