package com.project.teamsb.api

data class ResultLogin(
    var check: Boolean = false,
    var code: Int = 0,
    var id: String = "",
    var password: String = "",
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
    var writeUser : String = "",
    var text : String = "",
    var hash: List<String>)


