package com.project.teamsb.data.remote.notice

import com.project.teamsb.api.*
import com.project.teamsb.data.entities.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NoticeService {

    @GET("/notice/list/")
    suspend fun noticeList(
        @Query("page") page:Int
    ): Response<NoticeResponse>
    @GET("/notice/list/top/")
    suspend fun noticeTopList(
    ): Response<NoticeResponse>
}