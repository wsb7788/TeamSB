package com.project.teamsb.main.home

import retrofit2.Call
import retrofit2.http.*

interface PostAPI {
    @GET("/recentPost")
    fun recentPost(
    ) : Call<ResultPost>

    @GET
    fun categoryPost(
        @Url url: String,
        @Query("page") page: Int
    ) : Call<ResultPost>




}