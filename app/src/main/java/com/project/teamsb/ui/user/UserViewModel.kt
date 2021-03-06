package com.project.teamsb.ui.user

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
import com.project.teamsb.api.ResultNickname
import com.project.teamsb.api.ResultNotiCheck
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ServerObj
import com.project.teamsb.data.entities.Content
import com.project.teamsb.data.remote.main.MainListener
import com.project.teamsb.data.remote.nickname.NicknameListener
import com.project.teamsb.data.remote.user.UserListener
import com.project.teamsb.data.repository.login.LoginRepository
import com.project.teamsb.data.repository.main.MainRepository
import com.project.teamsb.data.repository.nickname.NicknameRepository
import com.project.teamsb.data.repository.user.UserRepository
import com.project.teamsb.main.home.HomeFragment
import com.project.teamsb.main.notice.NoticeFragment
import com.project.teamsb.main.user.UserFragment
import com.project.teamsb.post.App
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

class UserViewModel(private val repository: UserRepository, private val sharedPreferencesManager: SharedPreferencesManager):ViewModel() {


    var userListener: UserListener? = null
    val page = MutableLiveData<Int>(1)
    var liveData = MutableLiveData<ArrayList<PostModel>>()
    var loadLock = MutableLiveData<Boolean>(false)
    var noMoreItem = MutableLiveData<Boolean>(false)
    var isRefresh = MutableLiveData<Boolean>(false)

    fun userPostLoading() {
        userListener?.onLoadingStarted()
        loadLock.value = true
        Coroutines.main {

            try {
                val id = sharedPreferencesManager.getId()
                val userPostResponse = repository.myArticleList(page.value!!.toInt(),id)
                if(userPostResponse.code == 200){
                    setPost(userPostResponse.content)
                    return@main
                }
                userListener?.onLoadingEnd()
                userListener?.apiFailure(userPostResponse.message)
                loadLock.value = false
            }catch (e:Exception){
                Log.d("??????",e.message!!)
            }
        }
    }

    private fun setPost(response: ArrayList<Content>) {
        if (response.size>0) userListener?.existPost()
        var data = ArrayList<PostModel>()
        if (response.size % 10 != 0 || response.isEmpty()) {
            noMoreItem.value = true
        }
        for (i in response.indices) {
            val nickname = response[i].userNickname
            val category = response[i].category
            val title = response[i].title
            val keywords = response[i].hash

            var profileImage = Utils.convertImageStringToBitmap(response[i].imageSource)

            val timeCreated = response[i].timeStamp        // yyyy-MM-dd hh:mm:ss
            var timeStamp = Utils.convertTimeStamp(timeCreated)

            val comment = response[i].replyCount
            val no = response[i].no
            val myModel = PostModel(title,keywords,timeStamp,nickname,comment,category,no,profileImage)
            data.add(myModel)
        }
        liveData.postValue(data)
        userListener?.onLoadingEnd()
        loadLock.value = false
    }

    fun pageplus() {
        page.value = page.value!! + 1
    }

    fun pageInit() {
        page.value = 1
    }
}