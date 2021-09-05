package com.project.teamsb.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.teamsb.api.*
import com.project.teamsb.data.entities.Content
import com.project.teamsb.data.remote.home.HomeListener
import com.project.teamsb.data.repository.home.HomeRepository
import com.project.teamsb.utils.Coroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class HomeViewModel(private val repository: HomeRepository):ViewModel() {


    var homeListener: HomeListener? = null

    var liveData = MutableLiveData<ArrayList<Content>>()


    fun bannerLoading() {
        Coroutines.main {
            try {
                val bannerResponse = repository.topBanner()
                if(bannerResponse.code == 200){
                    homeListener?.setBanner(bannerResponse.topBannerList)
                    return@main
                }
                homeListener?.apiFailure(bannerResponse.message)
            }catch (e: Exception){
                Log.d("로그",e.message!!)
            }
        }
    }

    fun recentPostLoading() {
        Coroutines.main {
            try {
                val recentResponse = repository.recentPost()
                if(recentResponse.code == 200){
                    var data = ArrayList<Content>()
                    for(i in recentResponse.content){
                        data.add(i)
                    }
                    liveData.postValue(data)
                }
            } catch (e:Exception){
                Log.d("로그",e.message!!)
            }
        }
    }

    fun calendarLoading() {
        Coroutines.main {
            try{
                val calendarResponse = repository.getCalendar()

                if (calendarResponse.code ==200 ){
                    homeListener?.setCalendar(calendarResponse.menu)
                    return@main
                }
                homeListener?.apiFailure(calendarResponse.message)
            }catch (e:Exception){
                Log.d("로그",e.message!!)
            }
        }
    }

    fun guideLoading() {
        Coroutines.main {
            try{
                val calendarResponse = repository.guideList()

                if (calendarResponse.code ==200 ){
                    homeListener?.setGuide(calendarResponse.content)
                    return@main
                }
                homeListener?.apiFailure(calendarResponse.message)
            }catch (e:Exception){
                Log.d("로그",e.message!!)
            }
        }
    }


}