package com.project.teamsb.recycler.model

import android.graphics.Bitmap
import android.util.Log

class NotificationModel(var nickname: String? = null, val content: String? = null, val timeStamp: String?= null, val profileImage: Bitmap?=null) {
    val TAG: String = "로그"


    //기본 생성자자
    init{
        Log.d(TAG, "MyModel - init() called")
    }
}