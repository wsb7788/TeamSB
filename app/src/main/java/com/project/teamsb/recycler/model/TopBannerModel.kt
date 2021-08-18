package com.project.teamsb.recycler.model

import android.graphics.Bitmap
import android.util.Log

class TopBannerModel(val text:String?=null) {
    val TAG: String = "로그"


    //기본 생성자자
    init{
        Log.d(TAG, "MyModel - init() called")
    }
}