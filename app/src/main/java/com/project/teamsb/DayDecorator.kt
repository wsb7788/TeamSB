package com.project.teamsb

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class DayDecorator : DayViewDecorator  {

    var date= CalendarDay.today()

//    fun DayDecorator() {
//        date = CalendarDay.today()
//    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day!! == date
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.addSpan(StyleSpan(Typeface.BOLD))
        view!!.addSpan(ForegroundColorSpan(Color.parseColor("#F9DA78")))
    }

//    fun setDate(date: Date){
//        this.date = CalendarDay.from(date)
//    }

}