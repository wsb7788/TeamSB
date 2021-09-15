package com.project.teamsb.data.repository.main

import com.project.teamsb.data.remote.main.MainService
import com.project.teamsb.data.remote.main.NicknameResponse
import com.project.teamsb.data.remote.main.NotiCheckResponse
import com.project.teamsb.data.repository.BaseRepository

class MainRepository(private val mainService: MainService): BaseRepository() {
    suspend fun getUserNickname(id: String): NicknameResponse{
        return apiRequest { mainService.getUserNickname(id) }
    }
    suspend fun notiCheck(id: String): NotiCheckResponse{
        return apiRequest { mainService.notiCheck(id) }
    }
}