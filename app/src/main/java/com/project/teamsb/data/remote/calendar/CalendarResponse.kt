package com.project.teamsb.data.remote.calendar

import com.project.teamsb.data.entities.GetMenu


data class CalendarResponse(
    var check: Boolean,
    var code: Int,
    var menu: ArrayList<GetMenu>,
    var message: String)


