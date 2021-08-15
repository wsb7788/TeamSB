package com.project.teamsb.api

import retrofit2.Call
import retrofit2.http.*

interface ServerAPI {
    @FormUrlEncoded
    @POST("/login/")
    fun requestLogin(
        @Field("userId") userId:String,
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
        @Field("curId") curId:String,
        @Field("nickname") nickname:String
    ) : Call<NicknameSet>


    @GET
    fun recentPost(
        @Url url: String
    ) : Call<ResultPost>

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
        @Field("userId") userID: String,
        @Field("text") text: String,
        @Field("hash") hash: ArrayList<String>
    ) : Call<ResultWrite>

    @FormUrlEncoded
    @POST("/modifyArticle/")
    fun modifyArticle(
        @Field("curUser") curUser: String,
        @Field("title") title: String,
        @Field("category") category: String,
        @Field("text") text: String,
        @Field("hash") hash: ArrayList<String>,
        @Field("no") no: Int
    ) : Call<ResultWrite>

    @FormUrlEncoded
    @POST("/search/")
    fun search(
        @Query("page") page:Int,
        @Field("category") category: String,
        @Field("keyword") keyword: String
    ) : Call<ResultPost>

    @FormUrlEncoded
    @POST("/reply/list/")
    fun replyList(
        @Query("page") page:Int,
        @Field("article_no") article_no: Int,
        @Field("curUser") curUser: String
    ) : Call<ResultReply>

    @FormUrlEncoded
    @POST("/accessArticle/detail/")
    fun detail(
        @Field("no") no: Int
    ) : Call<ResultPost>

    @FormUrlEncoded
    @POST("/accessArticle/")
    fun accessArticle(
        @Field("no") no: Int
    ) : Call<ResultNoReturn>

    @FormUrlEncoded
    @POST("/reply/write/")
    fun writeComment(
        @Field("article_no") article_no: Int,
        @Field("content") content: String,
        @Field("curUser") curUser: String
        ) : Call<ResultNoReturn>

    @FormUrlEncoded
    @POST("/getUser/nickname/")
    fun getUserNickname(
        @Field("id") id: String,
    ) : Call<ResultNickname>

    @FormUrlEncoded
    @POST("/getUser/profile_image/")
    fun getUserProfileImage(
        @Field("curId") curId: String,
    ) : Call<ResultProfileImage>

    @FormUrlEncoded
    @POST("/check_mod/")
    fun checkMod(
        @Field("curUser") curUser: String,
        @Field("no") no: Int,
    ) : Call<ResultNoReturn>

    @FormUrlEncoded
    @POST("/deleteArticle/")
    fun deleteArticle(
        @Field("curUser") curUser: String,
        @Field("no") no: Int,
    ) : Call<ResultNoReturn>

    @FormUrlEncoded
    @POST("/report/")
    fun report(
        @Field("curUser") curUser: String,
        @Field("article_no") no: Int,
        @Field("content") content: String
    ) : Call<ResultNoReturn>

    @GET("/calmenu/")
    fun getCalendar(): Call<GetCalendar>

    @FormUrlEncoded
    @POST("/feedback/")
    fun feedback(
        @Field("curUser") curUser: String,
        @Field("text") text: String,
    ) : Call<ResultNoReturn>
    @FormUrlEncoded
    @POST("/profileSet/")
    fun profileSet(
        @Field("curId") curId: String,
        @Field("profile_image") profile_image: String,
    ) : Call<ResultNoReturn>

    @FormUrlEncoded
    @POST("/getToken/")
    fun getToken(
        @Field("curUser") curUser: String,
        @Field("token") token: String
    ) : Call<ResultNoReturn>

    @FormUrlEncoded
    @POST
    fun notiCheck(
        @Url url: String,
        @Field("curUser") curUser: String
    ) : Call<ResultNotiCheck>

    @FormUrlEncoded
    @POST("/notification/list/")
    fun notiList(
        @Query("page") page:Int,
        @Field("curUser") curUser: String
    ) : Call<ResultNotiList>

    @FormUrlEncoded
    @POST("/notification/read/")
    fun notiRead(
        @Field("curUser") curUser: String,
        @Field("notification_no") notification_no:Int
    ) : Call<ResultNoReturn>
    @FormUrlEncoded
    @POST("/notification/read/all/")
    fun notiReadAll(
        @Field("curUser") curUser: String,
    ) : Call<ResultNoReturn>
}