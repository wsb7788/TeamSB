package com.project.teamsb.data.repository.calendar

import com.project.teamsb.data.remote.calendar.CalendarResponse
import com.project.teamsb.data.remote.calendar.CalendarService
import com.project.teamsb.data.remote.home.*
import com.project.teamsb.data.remote.login.LoginResponse
import com.project.teamsb.data.remote.login.LoginService
import com.project.teamsb.data.repository.BaseRepository
import retrofit2.Response
import retrofit2.http.GET

class CalendarRepository(private val calendarService: CalendarService): BaseRepository() {
    suspend fun getCalendar(): CalendarResponse{
        return apiRequest { calendarService.getCalendar() }
    }

}