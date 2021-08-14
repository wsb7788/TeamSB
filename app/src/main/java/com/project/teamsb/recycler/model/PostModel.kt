package com.project.teamsb.recycler.model

import android.graphics.Bitmap
import android.util.Log
import com.project.teamsb.api.Content

class PostModel(var title: String? = null, var text: String? = null, var time: String? = null,var nickname: String? =  null,
                var comment:String? = null,var category: String? = null,  var no: Int = 0,var profileImage: Bitmap? = null) {
    val TAG: String = "로그"


    //기본 생성자자
    init{
        Log.d(TAG, "MyModel - init() called")
    }
}