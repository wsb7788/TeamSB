package com.project.teamsb.recycler.model

import android.util.Log

class CommentModel(var nickname: String? = null, var id: Boolean? = false, val content: String? = null) {
    val TAG: String = "로그"


    //기본 생성자자
    init{
        Log.d(TAG, "MyModel - init() called")
    }
}