package com.project.teamsb.data.remote.calendar


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CalendarService {
    @GET("/calmenu/")
    suspend fun getCalendar(): Response<CalendarResponse>
}