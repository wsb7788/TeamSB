package com.project.teamsb.data.entities

data class Content(
    var no: Int = 0,
    var title: String ="",
    var category: String = "",
    var timeStamp : String = "",
    var mod_timeStamp : String = "",
    var userId : String = "",
    var userNickname : String = "",
    var text : String = "",
    var viewCount : String = "",
    var reportCount : String = "",
    var replyCount : String = "",
    var hash: List<String>,
    var imageSource: String = "")