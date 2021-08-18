package com.project.teamsb.recycler.model

import android.graphics.Bitmap
import android.util.Log
import com.project.teamsb.api.Content

class NoticeModel(var notice_no:String? = null, var title:String? = null, var content:String? = null,
                  var viewCount:String?=null, var timeStamp:String?=null, val realTop:Boolean? = false) {
    val TAG: String = "로그"


    //기본 생성자자
    init{

        Log.d(TAG, "MyModel - init() called")
    }
}