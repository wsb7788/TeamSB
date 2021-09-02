package com.project.teamsb.data.remote.nickname

data class NicknameCheckResult(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")

data class NicknameSetResult(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")