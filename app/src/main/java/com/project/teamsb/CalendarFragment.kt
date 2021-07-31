package com.project.teamsb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.project.teamsb.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CalendarFragment : Fragment(), OnDateSelectedListener{

    lateinit var binding: FragmentCalendarBinding

//    var retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl("http://13.209.10.30:3000/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    var calenderservice: CalenderAPI = retrofit.create(CalenderAPI::class.java)


//    fun getTodayDate(): Int {
//        val calender_today = Calendar.getInstance()
//        val Tdate = calender_today.get(Calendar.DATE)
//        val Tyear = calender_today.get(Calendar.YEAR).toString()
//        val Tmonth = (calender_today.get(Calendar.MONTH) + 1).toString()
//        val Tdate = calender_today.get(Calendar.DATE).toString()
//
//        val today = Tyear + "/" + Tmonth + "/" + Tdate
//        return (Tdate-1)
//    }

//    override fun onSelectedDayChange(p0: CalendarView, year: Int, month: Int, date: Int) {
//    }


        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?,
        ): View? {

            binding = FragmentCalendarBinding.inflate(inflater, container, false)
            //binding.calender.setOnDateChangeListner(this)
            //binding.materialCalender.setOnDateChangedListener(this)
            //캘린더에서 오늘 날짜 불러오기
            var startTimeCalendar = Calendar.getInstance()
            //var endTimeCalendar = Calendar.getInstance()
            val currentYear = startTimeCalendar.get(Calendar.YEAR)
            val currentMonth = startTimeCalendar.get(Calendar.MONTH)
            val currentDate = startTimeCalendar.get(Calendar.DATE)
            val current = CalendarDay.from(currentYear, currentMonth, currentDate)

            //실행시 오늘 날짜 선택
            binding.materialCalender.selectedDate = current

            //이번달만 보여주기기
           binding.materialCalender.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(currentYear, currentMonth, 1))
                .setMaximumDate(CalendarDay.from(currentYear, currentMonth, startTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

            //API
            CalenderObj.api.getCalender().enqueue(object : Callback<GetCalender> {
                override fun onResponse(call: Call<GetCalender>, response: Response<GetCalender>) {
                    var getCalender = response.body()
                    Log.d("call", "API")
                    var arr = getCalender?.menu

                    //처음 실행시 오늘 식단 보여주기
                    val Tdate = currentDate -1
                    var breakfast = arr?.get(Tdate)?.아침?.get(0)?.joinToString("\n")
                    var lunch1 = arr?.get(Tdate)?.점심?.get(0)?.joinToString("\n")
                    var lunch2 = arr?.get(Tdate)?.점심?.get(1)?.joinToString("\n")
                    var dinner = arr?.get(Tdate)?.저녁?.get(0)?.joinToString("\n")

                    binding.breakfastTv.setText("[아침]\n" + breakfast)
                    binding.lunchTv1.setText("[점심1]\n" + lunch1)
                    binding.lunchTv2.setText("[점심2]\n" + lunch2)
                    binding.dinnerTv.setText("[저녁]\n" + dinner)

//                Log.d("code", "${getCalender?.code}")
                  Log.d("check", "${getCalender?.check}")
//                Log.d("menu", "${getCalender?.menu}")
//                Log.d("date", "${getCalender?.menu?.get(2)}")
                    //binding.meterialCalender.setOnDateChangedListener{ materialCalendarView: MaterialCalendarView, calendarDay: CalendarDay, b: Boolean ->

                    //binding.calender.setOnDateChangedListener{ calendarView: CalendarView, year: Int, month: Int, date: Int ->
                     //   Log.d("ymd", "$year ${month + 1} $date")

                    //날짜 변경시 식단 바꾸기
                    binding.materialCalender.setOnDateChangedListener { widget, date, selected ->

                        Log.d("dd","$widget $date $selected")
                        Log.d("d","${binding.materialCalender.selectedDate.date} $currentDate")
                        //오늘 날짜 색 지정
                        var deco = DayDecorator()
                        binding.materialCalender.addDecorators(deco)
                        if(date.day==(currentDate)) {
                            Log.d("샂제","제발 ${date.day}")
                            binding.materialCalender.removeDecorators()
                            Log.d("샂제","제발")
                        }




                        Log.d("day","${date.day}")
                        var day = date.day - 1

                        var breakfast = arr?.get(day)?.아침?.get(0)?.joinToString("\n")
                        var lunch1 = arr?.get(day)?.점심?.get(0)?.joinToString("\n")
                        var lunch2 = arr?.get(day)?.점심?.get(1)?.joinToString("\n")
                        var dinner = arr?.get(day)?.저녁?.get(0)?.joinToString("\n")

                        binding.breakfastTv.setText("[아침]\n" + breakfast)
                        binding.lunchTv1.setText("[점심1]\n" + lunch1)
                        binding.lunchTv2.setText("[점심2]\n" + lunch2)
                        binding.dinnerTv.setText("[저녁]\n" + dinner)
                    }
                }

                override fun onFailure(call: Call<GetCalender>, t: Throwable) {
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

