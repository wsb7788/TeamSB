package com.project.teamsb.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import com.project.teamsb.api.GetCalendar
import com.project.teamsb.api.ServerObj
import com.project.teamsb.data.entities.GetMenu
import com.project.teamsb.data.remote.calendar.CalendarListener
import com.project.teamsb.databinding.FragmentCalendarBinding
import com.project.teamsb.main.calendar.DayDecorator
import com.project.teamsb.main.calendar.SunDayDecorator
import com.project.teamsb.ui.BaseFragment
import com.project.teamsb.ui.calendar.CalendarViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class CalendarFragment : BaseFragment(), CalendarListener{

    private  var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalendarViewModel by viewModel()

    val deco = DayDecorator()
    val decoSunday = SunDayDecorator()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.calendarListener = this

        calendarInit()

        viewModel.getCalendar()


        return binding.root
    }


    override fun setCalendar(menu: ArrayList<GetMenu>) {

        val tDate = viewModel.current.value!!.day -1
        var breakfast = menu[tDate].아침[0].joinToString("\n")
        var lunch1 = menu[tDate].점심[0].joinToString("\n")
        var lunch2 = menu[tDate].점심[1].joinToString("\n")
        var dinner = menu[tDate].저녁[0].joinToString("\n")

        //오늘 식단 textView에 넣기
        binding.breakfastTv.text = breakfast
        binding.lunchTv1.text = lunch1
        binding.lunchTv2.text = lunch2
        binding.dinnerTv.text = dinner

        binding.materialCalendar.setOnDateChangedListener { widget, date, selected ->
            if(date.day==(viewModel.current.value!!.day)) {
                binding.materialCalendar.removeDecorators()
                binding.materialCalendar.addDecorator(decoSunday)
            }else{
                binding.materialCalendar.addDecorator(deco)
                binding.materialCalendar.addDecorator(decoSunday)
            }

            var day = date.day - 1
            //선택된 날짜의 식단 가져오기
            var breakfast = menu[day].아침[0].joinToString("\n")
            var lunch1 = menu[day].점심[0].joinToString("\n")
            var lunch2 = menu[day].점심[1].joinToString("\n")
            var dinner = menu[day].저녁[0].joinToString("\n")

            //불러온 식단 textView에 넣기
            binding.breakfastTv.text = breakfast
            binding.lunchTv1.text = lunch1
            binding.lunchTv2.text = lunch2
            binding.dinnerTv.text = dinner
        }
    }

    override fun calendarInit() {
        var startTimeCalendar = Calendar.getInstance()
        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth = startTimeCalendar.get(Calendar.MONTH)
        val currentDate = startTimeCalendar.get(Calendar.DATE)
        viewModel.current.value = CalendarDay.from(currentYear, currentMonth, currentDate)

        binding.materialCalendar.selectedDate = viewModel.current.value
        //달력설정(시작일을 일요일으로, 이번달만 보여주기)
        binding.materialCalendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(currentYear, currentMonth, 1))
            .setMaximumDate(CalendarDay.from(currentYear, currentMonth, startTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()
        //오늘 날짜 노란색으로 바꾸기
        binding.materialCalendar.addDecorators(decoSunday)
    }
    override fun apiFailure(message: String) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }




}

