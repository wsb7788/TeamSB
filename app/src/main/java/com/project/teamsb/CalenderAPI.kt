package com.project.teamsb

import retrofit2.Call
import retrofit2.http.GET

interface CalenderAPI {
    @GET("/calmenu/")
   // fun getMenu(): Call<GetMenu>
    fun getCalender(): Call<GetCalender>
}