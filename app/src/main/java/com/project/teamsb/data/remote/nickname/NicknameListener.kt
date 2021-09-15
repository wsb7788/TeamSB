package com.project.teamsb.data.remote.nickname

interface NicknameListener {
    fun onNicknameCheckResponse(message: String)
    fun onNicknameSetFailure(message: String)
    fun onNicknameSetSuccess()
}