package com.project.teamsb.data.entities

data class NoticeContent(
    var notice_no: Int,
    var title:String,
    var content:String,
    var viewCount: Int,
    var timeStamp: String,
    var fixTop: Boolean,
    var realTop:Boolean)