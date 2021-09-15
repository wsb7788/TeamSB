package com.project.teamsb.data.remote.main

interface MainListener {
    fun backPressedMessage(message: String)
    fun finish()
    fun botttomInit()
    fun onNicknameFailure(message: String)
    fun existNoti()
    fun noExistNoti()
    fun onNotiFailure(message: String)
}