package com.project.teamsb.api

import retrofit2.Call
import retrofit2.http.*

interface ServerAPI {
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


    @GET("/recentPost")
    fun recentPost() : Call<ResultPost>

    @GET
    fun categoryPost(
        @Url url: String,
        @Query("page") page: Int
    ) : Call<ResultPost>

    @FormUrlEncoded
    @POST("/writeArticle/")
    fun writeArticle(
        @Field("title") title: String,
        @Field("category") category: String,
        @Field("userID") userID: String,
        @Field("text") text: String,
        @Field("hash") hash: List<String>
    ) : Call<ResultWrite>


}