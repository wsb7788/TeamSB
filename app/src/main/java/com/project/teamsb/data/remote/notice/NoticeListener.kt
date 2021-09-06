package com.project.teamsb.data.remote.notice

import com.project.teamsb.data.entities.GetMenu
import com.project.teamsb.data.entities.Guide

interface NoticeListener {
    fun recyclerInit()
    fun apiFailure(message: String)


}