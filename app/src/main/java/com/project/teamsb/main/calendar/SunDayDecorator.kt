package com.project.teamsb.main.calendar

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class SunDayDecorator : DayViewDecorator {
//오늘 날짜 색변경
    val calendar = Calendar.getInstance()


    fun SundayDecorator() {
    }
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        day!!.copyTo(calendar)
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)

        return  weekDay == Calendar.SUNDAY
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.addSpan(ForegroundColorSpan(Color.parseColor("#FF0000")))
    }

}