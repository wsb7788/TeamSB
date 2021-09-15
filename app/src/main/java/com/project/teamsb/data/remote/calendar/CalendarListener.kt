package com.project.teamsb.data.remote.calendar

import com.project.teamsb.data.entities.GetMenu
import com.project.teamsb.data.entities.Guide

interface CalendarListener {
    fun apiFailure(message: String)
    fun setCalendar(menu: ArrayList<GetMenu>)
    fun calendarInit()


}