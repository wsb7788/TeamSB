package com.project.teamsb.data.repository.home

import com.project.teamsb.data.remote.home.*
import com.project.teamsb.data.remote.login.LoginResponse
import com.project.teamsb.data.remote.login.LoginService
import com.project.teamsb.data.repository.BaseRepository
import retrofit2.Response
import retrofit2.http.GET

class HomeRepository(private val homeService: HomeService): BaseRepository() {
    suspend fun topBanner(): BannerResponse{
        return apiRequest { homeService.topBanner() }
    }

    suspend fun guideList() : GuideResponse{
        return apiRequest { homeService.guideList() }
    }

    suspend fun getCalendar(): CalendarResponse{
        return apiRequest { homeService.getCalendar() }
    }

    suspend fun recentPost() : PostResponse{
        return apiRequest { homeService.recentPost() }
    }

}