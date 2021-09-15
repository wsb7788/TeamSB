package com.project.teamsb.data.remote.login

data class LoginResponse(
    var check: Boolean = false,
    var code: Int = 0,
    var nickname: Boolean = false,
    var message: String = "")