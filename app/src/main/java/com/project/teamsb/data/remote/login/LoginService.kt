package com.project.teamsb.data.remote.login

import com.project.teamsb.data.entities.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface LoginService {
    @FormUrlEncoded
    @POST("/login/")
    suspend fun login(@Field("userId") userId:String,
                      @Field("password") password:String
    ): Response<LoginResponse>
}