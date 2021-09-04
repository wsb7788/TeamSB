package com.project.teamsb.data.remote.home

import com.project.teamsb.api.GetCalendar
import com.project.teamsb.api.ResponseBanner
import com.project.teamsb.api.ResultGuide
import com.project.teamsb.api.ResultPost
import com.project.teamsb.data.entities.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface HomeService {
    @GET("/topBanner/")
    suspend fun topBanner(
    ): Response<BannerResponse>

    @GET("/guide/list/")
    suspend fun guideList() : Response<GuideResponse>

    @GET("/calmenu/")
    suspend fun getCalendar(): Response<CalendarResponse>

    @GET("/home/recentPost/")
    fun recentPost() : Response<PostResponse>

}