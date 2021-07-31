package com.project.teamsb.main.calendar

import retrofit2.Call
import retrofit2.http.GET

interface CalendarAPI {
    @GET("/calmenu/")
    fun getCalendar(): Call<GetCalendar>
}