package com.project.teamsb.login

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

data class test(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")



