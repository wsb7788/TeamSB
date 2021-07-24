package com.project.teamsb.login

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.POST

interface LoginAPI {
    @FormUrlEncoded
    @POST("/login/")
    fun requestLogin(
        @Field("id") id:String,
        @Field("password") password:String
    ) : Call<ResultLogin>

    @FormUrlEncoded
    @POST("/nicknameCheck/")
    fun nicknameCheck(
        @Field("nickname") nickname: String
    ) : Call<NicknameCheck>
    
    @FormUrlEncoded
    @POST("/nicknameSet/")
    fun nicknameSet(
        @Field("id") id:String,
        @Field("nickname") nickname:String
    ) : Call<NicknameSet>

    @FormUrlEncoded
    @POST("/test/")
    fun test(
        @Field("id") id:String,
        @Field("nickname") nickname:String
    ) : Call<test>




}