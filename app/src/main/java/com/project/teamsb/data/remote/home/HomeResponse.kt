package com.project.teamsb.data.remote.home

data class BannerResponse(
var check:Boolean,
var code:Int,
var message: String,
var topBannerList: List<String>)

data class GuideResponse(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: ArrayList<Guide>)

data class Guide(
    var guide_no:Int,
    var title:String,
    var content: String,
    var timeStamp: String)

data class CalendarResponse(
    var check: Boolean,
    var code: Int,
    var menu: ArrayList<GetMenu>)

data class GetMenu(
    var 일자: String,
    var 아침: ArrayList<ArrayList<String>>,
    var 점심: ArrayList<ArrayList<String>>,
    var 저녁: ArrayList<ArrayList<String>>)

data class PostResponse(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: List<Content>)

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