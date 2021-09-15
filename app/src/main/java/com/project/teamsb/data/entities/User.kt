package com.project.teamsb.data.entities

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName(value = "userId") val userId: String?,
    @SerializedName(value = "password") val password: String?,
    )
