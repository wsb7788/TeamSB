package com.project.teamsb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.teamsb.api.GetCalendar
import com.project.teamsb.databinding.FragmentCalendarBinding
import com.project.teamsb.main.calendar.CalendarObj
import com.project.teamsb.main.calendar.DayDecorator
import com.project.teamsb.main.calendar.SunDayDecorator
import com.prolificinteractive.materialcalendarview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CalendarFragment : Fragment(), OnDateSelectedListener{

    lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        //캘린더에서 오늘 날짜 불러오기
        var startTimeCalendar = Calendar.getInstance()
        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth = startTimeCalendar.get(Calendar.MONTH)
        val currentDate = startTimeCalendar.get(Calendar.DATE)
        val current = CalendarDay.from(currentYear, currentMonth, currentDate)

        //실행시 오늘 날짜 선택
        binding.materialCalendar.selectedDate = current
        //달력설정(시작일을 일요일으로, 이번달만 보여주기)
        binding.materialCalendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(currentYear, currentMonth, 1))
            .setMaximumDate(CalendarDay.from(currentYear, currentMonth, startTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()
        //오늘 날짜 노란색으로 바꾸기
        var deco = DayDecorator()
        var decoSunday = SunDayDecorator()
        binding.materialCalendar.addDecorators(decoSunday)

        //API
        CalendarObj.api.getCalendar().enqueue(object : Callback<GetCalendar> {
            override fun onResponse(call: Call<GetCalendar>, response: Response<GetCalendar>) {
                var getCalendar = response.body()
                var arr = getCalendar?.menu
                //처음 실행시 오늘 식단 보여주기
                val Tdate = currentDate -1
                var breakfast = arr?.get(Tdate)?.아침?.get(0)?.joinToString("\n")
                var lunch1 = arr?.get(Tdate)?.점심?.get(0)?.joinToString("\n")
                var lunch2 = arr?.get(Tdate)?.점심?.get(1)?.joinToString("\n")
                var dinner = arr?.get(Tdate)?.저녁?.get(0)?.joinToString("\n")

                //오늘 식단 textView에 넣기
                binding.breakfastTv.text = "아침 (07:00~08:30)\n$breakfast"
                binding.lunchTv1.text = "점심 (11:50~13:30)\n$lunch1"
                binding.lunchTv2.text = "\nlunch2"
                binding.dinnerTv.text = "저녁 (18:00~19:30)\n$dinner"

                //날짜 변경시 식단 바꾸기
                binding.materialCalendar.setOnDateChangedListener { widget, date, selected ->




                    //오늘 클릭되면 다시 흰색으로
                    if(date.day==(currentDate)) {
                        binding.materialCalendar.removeDecorators()
                        binding.materialCalendar.addDecorator(decoSunday)
                    }else{
                        binding.materialCalendar.addDecorator(deco)
                        binding.materialCalendar.addDecorator(decoSunday)
                    }

                    var day = date.day - 1

                    //선택된 날짜의 식단 가져오기
                    var breakfast = arr?.get(day)?.아침?.get(0)?.joinToString("\n")
                    var lunch1 = arr?.get(day)?.점심?.get(0)?.joinToString("\n")
                    var lunch2= ""
                    for(i in 0 until arr?.get(day)?.점심?.get(1)?.size!!){
                        lunch2 += arr?.get(day)?.점심[1][i] + "\n"
                    }
                    var dinner = arr?.get(day)?.저녁?.get(0)?.joinToString("\n")

                    //불러온 식단 textView에 넣기
                    binding.breakfastTv.text = "아침 (11:50~13:30)\n$breakfast"
                    binding.lunchTv1.text = "점심 (11:50~13:30)\n$lunch1"
                    binding.lunchTv2.text = "[점심2]\n$lunch2"
                    binding.dinnerTv.text = "저녁 (11:50~13:30)\n$dinner"
                }
            }

            override fun onFailure(call: Call<GetCalendar>, t: Throwable) {
                Log.d("f", "실패")
            }

        })
        return binding.root
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) { }


}

