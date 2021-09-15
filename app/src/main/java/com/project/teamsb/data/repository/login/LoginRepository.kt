package com.project.teamsb.data.repository.login

import com.project.teamsb.data.remote.login.LoginResponse
import com.project.teamsb.data.remote.login.LoginService
import com.project.teamsb.data.repository.BaseRepository

class LoginRepository(private val loginService: LoginService): BaseRepository() {
    suspend fun login(id: String, pw: String): LoginResponse{
        return apiRequest { loginService.login(id, pw) }
    }

}