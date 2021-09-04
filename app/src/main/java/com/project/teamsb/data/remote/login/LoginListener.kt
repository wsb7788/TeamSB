package com.project.teamsb.data.remote.login

interface LoginListener {
    fun onLoginStarted()
    fun onStartMain()
    fun onStartNickname()
    fun onLoginFailure(message: String)
    fun backPressedMessage(message: String)
    fun finish()

}