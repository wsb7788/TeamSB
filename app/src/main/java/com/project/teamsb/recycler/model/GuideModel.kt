package com.project.teamsb.recycler.model

import android.graphics.Bitmap
import android.util.Log
import com.project.teamsb.api.Content

class GuideModel(var title:String? = null, var content:String? = null) {
    val TAG: String = "로그"


    //기본 생성자자
    init{

        Log.d(TAG, "MyModel - init() called")
    }
}