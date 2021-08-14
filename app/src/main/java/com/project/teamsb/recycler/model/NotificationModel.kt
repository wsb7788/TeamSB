package com.project.teamsb.recycler.model

import android.graphics.Bitmap
import android.util.Log

class NotificationModel(var notificationNo:Int?=0, var articleNo:Int?=0,var nickname:String? = null, var title:String? = null, var content:String?=null,
                        var checkRead:Boolean? = false, var timeStamp:String?=null, var imageSoruce: Bitmap?=null) {
    val TAG: String = "로그"


    //기본 생성자자
    init{
        Log.d(TAG, "MyModel - init() called")
    }
}