package com.project.teamsb.recycler.model

import android.util.Log

class PostModel(var title: String? = null, var content: String? = null, val time: String? = null) {
    val TAG: String = "로그"


    //기본 생성자자
    init{
        Log.d(TAG, "MyModel - init() called")
    }
}