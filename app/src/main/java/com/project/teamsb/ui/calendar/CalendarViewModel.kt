package com.project.teamsb.ui.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.teamsb.api.GetCalendar
import com.project.teamsb.api.ServerObj
import com.project.teamsb.data.entities.GetMenu
import com.project.teamsb.data.remote.calendar.CalendarListener
import com.project.teamsb.data.repository.calendar.CalendarRepository
import com.project.teamsb.utils.Coroutines
import com.prolificinteractive.materialcalendarview.CalendarDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewModel(private val repository: CalendarRepository):ViewModel() {

    var calendarListener: CalendarListener? = null

    val current = MutableLiveData<CalendarDay>()

    fun getCalendar() {



        Coroutines.main {
            try {
                val calendarResponse = repository.getCalendar()

                if(calendarResponse.code == 200){
                    calendarListener?.setCalendar(calendarResponse.menu)
                    return@main
                }
                calendarListener?.apiFailure(calendarResponse.message)

            }catch (e:Exception){
                Log.d("로그",e.message!!)
            }
        }
        ServerObj.api.getCalendar().enqueue(object : Callback<GetCalendar> {
            override fun onResponse(call: Call<GetCalendar>, response: Response<GetCalendar>) {
                var getCalendar = response.body()
                var arr = getCalendar?.menu
                //처음 실행시 오늘 식단 보여주기


                //날짜 변경시 식단 바꾸기


            }

            override fun onFailure(call: Call<GetCalendar>, t: Throwable) {
                Log.d("f", "실패")
            }

        })
    }

}