package com.project.teamsb.data.remote.notice

import com.project.teamsb.data.entities.NoticeContent


data class NoticeResponse(
    var check:Boolean,
    var code:Int,
    var message: String,
    var content: ArrayList<NoticeContent>)

