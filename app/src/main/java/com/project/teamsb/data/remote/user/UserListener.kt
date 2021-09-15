package com.project.teamsb.data.remote.user

import com.project.teamsb.data.entities.GetMenu
import com.project.teamsb.data.entities.Guide

interface UserListener {
    fun recyclerInit()
    fun apiFailure(message: String)
    fun onLoadingStarted()
    fun onLoadingEnd()
    fun existPost()

}