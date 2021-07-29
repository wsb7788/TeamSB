package com.project.teamsb.api

import android.app.ApplicationErrorReport

data class ResultLogin(
    var check: Boolean = false,
    var code: Int = 0,
    var nickname: Boolean = false,
    var message: String = "")

data class NicknameCheck(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")

data class NicknameSet(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")

data class ResultPost(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: List<Content>)

data class Content(
    var no: Int = 0,
    var title: String ="",
    var category: String = "",
    var timeStamp : String = "",
    var mod_timeStamp : String = "",
    var userId : String = "",
    var userNickname : String = "",
    var text : String = "",
    var viewCount : String = "",
    var reportCount : String = "",
    var hash: List<String>)

data class ResultWrite(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = ""
)

data class ResultReply(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: List<ReplyContent>)

data class ReplyContent(
    var reply_no: Int = 0,
    var article_no: Int,
    var content: String = "",
    var userId: String = "",
    var userNickname: String = "",
    var timeStamp: String = "",
    var mod_timeStamp: String = "",
    var right: Boolean = false)

data class ResultNoReturn(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")

