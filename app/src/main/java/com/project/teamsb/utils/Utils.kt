package com.project.teamsb.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object Utils {

    fun convertTimeStamp(timeCreated: String): String{
        val y = timeCreated.substring(0,4).toInt()
        val M = timeCreated.substring(5,7).toInt()
        val d = timeCreated.substring(8,10).toInt()
        val h = timeCreated.substring(11,13).toInt()
        val m = timeCreated.substring(14,16).toInt()
        val s = timeCreated.substring(17,19).toInt()
        var timeCreatedParsedDateTime = LocalDateTime.of(y,M,d,h,m,s)
        var timeCreatedParsedDate = LocalDate.of(y,M,d)

        if(ChronoUnit.HOURS.between(LocalDateTime.now(),timeCreatedParsedDateTime).toInt() ==0){
            return  (ChronoUnit.SECONDS.between(timeCreatedParsedDateTime, LocalDateTime.now()).toInt() / 60).toString() +"분 전"
        }else if(ChronoUnit.DAYS.between(timeCreatedParsedDate, LocalDate.now()).toInt() == 0)
            return  timeCreated.substring(11,16)
        else
            return  timeCreated.substring(5,10)
    }
    fun convertImageStringToBitmap(stringImage: String): Bitmap {

        val byteProfileImage = Base64.decode(stringImage,0)
        val inputStream = ByteArrayInputStream(byteProfileImage)
        return BitmapFactory.decodeStream(inputStream)

    }

}