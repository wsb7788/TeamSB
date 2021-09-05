package com.project.teamsb.data.remote.home

import com.project.teamsb.data.entities.GetMenu
import com.project.teamsb.data.entities.Guide

interface HomeListener {
    fun setBanner(topBannerList: List<String>)
    fun apiFailure(message: String)
    fun setCalendar(menu: ArrayList<GetMenu>)
    fun setGuide(content: ArrayList<Guide>)

}