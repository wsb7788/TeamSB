package com.project.teamsb.main.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CalendarObj {
    var api: CalendarAPI
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://13.209.10.30:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        api = retrofit.create(CalendarAPI::class.java)
    }
}