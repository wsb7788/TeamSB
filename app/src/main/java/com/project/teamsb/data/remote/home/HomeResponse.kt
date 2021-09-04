package com.project.teamsb.data.remote.home

import com.project.teamsb.data.entities.Content
import com.project.teamsb.data.entities.GetMenu
import com.project.teamsb.data.entities.Guide

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

data class CalendarResponse(
    var check: Boolean,
    var code: Int,
    var menu: ArrayList<GetMenu>)

data class PostResponse(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: List<Content>)

