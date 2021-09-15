package com.project.teamsb.data.remote.main

import com.project.teamsb.api.ResultNickname
import com.project.teamsb.api.ResultNotiCheck
import com.project.teamsb.data.entities.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MainService {
    @FormUrlEncoded
    @POST("/getUser/nickname/")
    suspend fun getUserNickname(
        @Field("id") id: String
    ) : Response<NicknameResponse>

    @FormUrlEncoded
    @POST("/notification/check/")
    suspend fun notiCheck(
        @Field("curUser") curUser: String
    ) : Response<NotiCheckResponse>
}