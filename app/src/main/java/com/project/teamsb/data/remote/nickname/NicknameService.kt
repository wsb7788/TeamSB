package com.project.teamsb.data.remote.nickname

import com.project.teamsb.api.NicknameCheck
import com.project.teamsb.api.NicknameSet
import com.project.teamsb.data.entities.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NicknameService {
    @FormUrlEncoded
    @POST("/nicknameCheck/")
    suspend fun nicknameCheck(
        @Field("nickname") nickname: String
    ) : Response<NicknameResponse>

    @FormUrlEncoded
    @POST("/nicknameSet/")
    suspend fun nicknameSet(
        @Field("curId") curId:String,
        @Field("nickname") nickname:String
    ) : Response<NicknameResponse>
}