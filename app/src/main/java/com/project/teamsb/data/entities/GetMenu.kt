package com.project.teamsb.data.entities

data class GetMenu(
    var 일자: String,
    var 아침: ArrayList<ArrayList<String>>,
    var 점심: ArrayList<ArrayList<String>>,
    var 저녁: ArrayList<ArrayList<String>>)