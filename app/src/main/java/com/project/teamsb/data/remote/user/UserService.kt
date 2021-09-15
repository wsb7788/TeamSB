package com.project.teamsb.data.remote.user

import com.project.teamsb.api.GetCalendar
import com.project.teamsb.api.ResponseBanner
import com.project.teamsb.api.ResultGuide
import com.project.teamsb.api.ResultPost
import com.project.teamsb.data.entities.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @FormUrlEncoded
    @POST("/myArticleList/")
    suspend fun myArticleList(
        @Query("page") page: Int,
        @Field("curUser") curUser: String
    ) : Response<PostResponse>

}