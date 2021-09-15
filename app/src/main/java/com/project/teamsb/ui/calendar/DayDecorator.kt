package com.project.teamsb.ui.calendar

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class DayDecorator : DayViewDecorator {
    //오늘 날짜 색변경
    var date = CalendarDay.today()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day!! == date
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.addSpan(StyleSpan(Typeface.BOLD))
        view!!.addSpan(ForegroundColorSpan(Color.parseColor("#F9DA78")))
    }

}