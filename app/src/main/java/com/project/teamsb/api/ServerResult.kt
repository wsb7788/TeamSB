package com.project.teamsb.api

data class ResultLogin(
    var check: Boolean = false,
    var code: Int = 0,
    var nickname: Boolean = false,
    var message: String = "")

data class NicknameCheck(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")

data class NicknameSet(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")

data class ResultPost(
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

data class ResultWrite(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = ""
)

data class ResultReply(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: List<ReplyContent>)

data class ReplyContent(
    var reply_no: Int = 0,
    var article_no: Int,
    var content: String = "",
    var userId: String = "",
    var userNickname: String = "",
    var imageSource: String = "",
    var timeStamp: String = "",
    var mod_timeStamp: String = "",
    var right: Boolean = false)

data class ResultNoReturn(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "")
data class ResultNickname(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: String = "")

data class ResultProfileImage(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: String = "")

data class GetCalendar(
    var check: Boolean,
    var code: Int,
    var menu: ArrayList<GetMenu>)

data class GetMenu(
    var 일자: String,
    var 아침: ArrayList<ArrayList<String>>,
    var 점심: ArrayList<ArrayList<String>>,
    var 저녁: ArrayList<ArrayList<String>>)

data class ResultNotiCheck(
    var check: Boolean,
    var code: Int,
    var message: String = "",
    var notificationCount:Int)

data class ResultNotiList(
    var check: Boolean,
    var code: Int,
    var message: String = "",
    var content:ArrayList<Notification>)

data class Notification(
    var notification_no: Int,
    var article_no: Int,
    var userId: String,
    var curUser: String,
    var nickname: String,
    var title: String,
    var content: String,
    var check_read: Boolean,
    var timeStamp: String,
    var imageSource: String)

data class ResponseNotice(
    var check:Boolean,
    var code:Int,
    var message: String,
    var content: ArrayList<NoticeContent>)

data class NoticeContent(
    var notice_no: Int,
    var title:String,
    var content:String,
    var viewCount: Int,
    var timeStamp: String,
    var fixTop: Boolean,
    var realTop:Boolean)

