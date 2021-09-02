package com.project.teamsb.data.remote.login

interface LoginListener {
    fun onLoginStarted()
    fun onLoginSuccess()
    fun onLoginFailure(message: String)
}