package com.project.teamsb.data.repository.user

import com.project.teamsb.data.remote.user.PostResponse
import com.project.teamsb.data.remote.user.UserService
import com.project.teamsb.data.repository.BaseRepository

class UserRepository(private val userService: UserService): BaseRepository() {
    suspend fun myArticleList(page: Int, id: String): PostResponse{
        return apiRequest { userService.myArticleList(page, id) }
    }


}