package com.project.teamsb.post

import android.util.Log

class CommentModel(var name: String? = null, var profileImage: Int? = null, val content: String? = null) {
    val TAG: String = "로그"


    //기본 생성자자
    init{
        Log.d(TAG, "MyModel - init() called")
    }
}