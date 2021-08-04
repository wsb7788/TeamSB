package com.project.teamsb.main.calendar

import com.project.teamsb.api.ServerAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CalendarObj {
    var api: ServerAPI
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://13.209.10.30:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        api = retrofit.create(ServerAPI::class.java)
    }
}