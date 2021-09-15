package com.project.teamsb.ui.notice

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.teamsb.CalendarFragment
import com.project.teamsb.R
import com.project.teamsb.api.*
import com.project.teamsb.data.entities.Content
import com.project.teamsb.data.entities.NoticeContent
import com.project.teamsb.data.remote.main.MainListener
import com.project.teamsb.data.remote.nickname.NicknameListener
import com.project.teamsb.data.remote.notice.NoticeListener
import com.project.teamsb.data.remote.user.UserListener
import com.project.teamsb.data.repository.login.LoginRepository
import com.project.teamsb.data.repository.main.MainRepository
import com.project.teamsb.data.repository.nickname.NicknameRepository
import com.project.teamsb.data.repository.notice.NoticeRepository
import com.project.teamsb.data.repository.user.UserRepository
import com.project.teamsb.main.home.HomeFragment
import com.project.teamsb.main.notice.NoticeFragment
import com.project.teamsb.main.user.UserFragment
import com.project.teamsb.post.App
import com.project.teamsb.recycler.model.NoticeModel
import com.project.teamsb.recycler.model.PostModel
import com.project.teamsb.utils.Coroutines
import com.project.teamsb.utils.SharedPreferencesManager
import com.project.teamsb.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class NoticeViewModel(private val repository: NoticeRepository, private val sharedPreferencesManager: SharedPreferencesManager):ViewModel() {


    var noticeListener: NoticeListener? = null
    val page = MutableLiveData<Int>(1)
    val topCount = MutableLiveData<Int>(0)
    var liveData = MutableLiveData<ArrayList<NoticeModel>>()
    var loadLock = MutableLiveData<Boolean>(false)
    var noMoreItem = MutableLiveData<Boolean>(false)
    var isRefresh = MutableLiveData<Boolean>(false)
    var isTopLoading = MutableLiveData<Boolean>(false)

    fun pageplus() {
        page.value = page.value!! + 1
    }

    fun pageInit() {
        page.value = 1
    }
    fun topNoticeLoading() {
        isTopLoading.value = true
        loadLock.value = true
        Coroutines.main {

            try {
                val topNoticeResponse = repository.noticeTopList()
                if(topNoticeResponse.code == 200){
                    topCount.value = topNoticeResponse.content.size
                    setNotice(topNoticeResponse.content)
                    pageInit()
                    noticeLoading()
                }

            }catch (e: Exception){
                Log.d("로그",e.message!!)
            }
        }
    }

    fun noticeLoading() {
        loadLock.value = true
        Coroutines.main {

            try {
                val noticeResponse = repository.noticeList(page.value!!.toInt())
                if(noticeResponse.code == 200){
                    if (noticeResponse.content.size % 10 != 0 || noticeResponse.content.isEmpty()) {
                        noMoreItem.value = true
                    }
                    setNotice(noticeResponse.content)
                }

            }catch (e: Exception){
                Log.d("로그",e.message!!)
            }
        }
    }

    private fun setNotice(content: ArrayList<NoticeContent>) {
        var modelList = ArrayList<NoticeModel>()
        for (i in 0 until content.size){

            val title = content[i].title
            val text = content[i].content
            val noticeNo = content[i].notice_no
            val viewCount = content[i].viewCount
            val topFix = content[i].realTop
            val timeStamp = content[i].timeStamp.substring(2,10).replace("-","/")

            val myModel = NoticeModel(noticeNo.toString(),title,text,viewCount.toString(),timeStamp,topFix)
            modelList.add(myModel)
        }

        liveData.postValue(modelList)
        if(!isTopLoading.value!!){
            loadLock.value = false
        }
    }
}