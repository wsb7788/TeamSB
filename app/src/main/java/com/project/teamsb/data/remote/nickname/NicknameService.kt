package com.project.teamsb.data.remote.nickname

import com.project.teamsb.api.NicknameCheck
import com.project.teamsb.api.NicknameSet
import com.project.teamsb.data.entities.User
import retrofit2.Call
import retrofit2.http.*

interface NicknameService {
    @FormUrlEncoded
    @POST("/nicknameCheck/")
    fun nicknameCheck(
        @Field("nickname") nickname: String
    ) : Call<NicknameCheckResult>

    @FormUrlEncoded
    @POST("/nicknameSet/")
    fun nicknameSet(
        @Field("curId") curId:String,
        @Field("nickname") nickname:String
    ) : Call<NicknameSetResult>
}