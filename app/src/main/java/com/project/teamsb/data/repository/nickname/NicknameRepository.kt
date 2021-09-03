package com.project.teamsb.data.repository.nickname

import com.project.teamsb.data.remote.login.LoginResponse
import com.project.teamsb.data.remote.nickname.NicknameResponse
import com.project.teamsb.data.remote.nickname.NicknameService
import com.project.teamsb.data.repository.BaseRepository

class NicknameRepository(private val nicknameService: NicknameService): BaseRepository() {
    suspend fun nicknameCheck(nickname:String): NicknameResponse {
        return apiRequest { nicknameService.nicknameCheck(nickname) }
    }
    suspend fun nicknameSet(id:String, nickname:String): NicknameResponse {
        return apiRequest { nicknameService.nicknameSet(id, nickname) }
    }
}