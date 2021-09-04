package com.project.teamsb.data.remote.home

interface HomeListener {
    fun onLoginStarted()
    fun onStartMain()
    fun onStartNickname()
    fun onLoginFailure(message: String)
    fun backPressedMessage(message: String)

}