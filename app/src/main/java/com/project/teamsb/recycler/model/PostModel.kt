package com.project.teamsb.recycler.model

import android.util.Log
import com.project.teamsb.api.Content

class PostModel(var title: String? = null, var text: String? = null, val time: String? = null,val nickname: String? =  null,
                val comment:String? = null,val category: String? = null,  val no: Int = 0) {
    val TAG: String = "로그"


    //기본 생성자자
    init{
        Log.d(TAG, "MyModel - init() called")
    }
}