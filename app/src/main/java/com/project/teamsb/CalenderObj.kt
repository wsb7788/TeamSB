package com.project.teamsb

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CalenderObj {
    var api: CalenderAPI
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://13.209.10.30:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        api = retrofit.create(CalenderAPI::class.java)
    }
}