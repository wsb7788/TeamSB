package com.project.teamsb.data.remote.user

import com.project.teamsb.data.entities.Content

data class PostResponse(
    var check: Boolean = false,
    var code: Int = 0,
    var message: String = "",
    var content: ArrayList<Content>)

