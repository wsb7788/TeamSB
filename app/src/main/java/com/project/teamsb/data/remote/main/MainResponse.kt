package com.project.teamsb.data.remote.main

data class NicknameResponse(
var check: Boolean = false,
var code: Int = 0,
var message: String = "",
var content: String = "")

data class NotiCheckResponse(
    var check: Boolean,
    var code: Int,
    var message: String = "",
    var notificationCount:Int)