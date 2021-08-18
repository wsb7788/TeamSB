package com.project.teamsb.main.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.project.teamsb.api.*
import com.project.teamsb.databinding.FragmentHomeBinding
import com.project.teamsb.main.calendar.CalendarObj
import com.project.teamsb.recycler.model.RecentModel
import com.project.teamsb.recycler.adapter.RecentRecyclerAdapter
import com.project.teamsb.recycler.adapter.TopBannerAdapter
import com.project.teamsb.recycler.model.TopBannerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentHomeBinding

    var bannerCount = 0
    var currentPosition = 0
    var intervalTime = 2000.toLong()
    var loadCount = 0



    var modelList = ArrayList<RecentModel>()
    var bannerList = ArrayList<TopBannerModel>()

    private lateinit var recentRecyclerAdapter: RecentRecyclerAdapter
    private lateinit var topBannerAdapter: TopBannerAdapter

    var isNotiCheck = false
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.tvRecentPost.setOnClickListener(this)
        recentRecyclerAdapter = RecentRecyclerAdapter()
        topBannerAdapter = TopBannerAdapter()
        binding.rcvRecent5.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = recentRecyclerAdapter
        }

        binding.vp2.adapter = topBannerAdapter
        binding.vp2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        bannerLoading()
        binding.srl.setOnRefreshListener {
            postLoading()
            calendarLoading()
            binding.srl.isRefreshing = false
        }
        return binding.root
    }

    private fun bannerLoading() {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                CalendarObj.api.topBanner().enqueue(object: Callback<ResponseBanner>{
                    override fun onResponse(call: Call<ResponseBanner>, response: Response<ResponseBanner>) {
                        if(response.body()!!.code ==200){
                            setbanner(response.body()!!.topBannerList)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBanner>, t: Throwable) {
                    }

                })

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setbanner(topBannerList: List<String>) {
        bannerCount = topBannerList.size
        currentPosition = Int.MAX_VALUE/(bannerCount-1)
        for(i in topBannerList.indices){
            val mymodel = TopBannerModel(topBannerList[i])
            bannerList.add(mymodel)
        }
        topBannerAdapter.submitList(bannerList)
        topBannerAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        postLoading()

        calendarLoading()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun calendarLoading() {

        CoroutineScope(Dispatchers.IO).launch {

            try{
                CalendarObj.api.getCalendar().enqueue(object :Callback<GetCalendar>{
                    override fun onResponse(call: Call<GetCalendar>, response: Response<GetCalendar>) {
                        setCalendar(response.body()!!.menu)
                    }

                    override fun onFailure(call: Call<GetCalendar>, t: Throwable) {
                    }

                })

            }catch(e: Exception){

            }

        }


    }

    private fun setCalendar(menu: ArrayList<GetMenu>) {
        val current = LocalDateTime.now()
        var formatter = DateTimeFormatter.ISO_DATE
        var formatted = current.format(formatter)
        for(i in menu){
            if(i.일자 == formatted){
                var now = LocalTime.now()
                var breakfast = LocalTime.of(8,30,0)
                var lunch =  LocalTime.of(13,30,0)
                var dinner =  LocalTime.of(19,30,0)
                when {
                    now.isBefore(breakfast) -> {
                        binding.foodtv.text = "아침(07:00~08:30)"
                         if(!i.아침[0].isNullOrEmpty()) {binding.tvConnerA.text = i.아침[0].joinToString ("\n")}

                    }
                    now.isBefore(lunch) -> {
                        binding.foodtv.text = "점심(11:50~13:30)"
                        if(!i.아침[0].isNullOrEmpty()) {binding.tvConnerA.text = i.점심[0].joinToString ("\n")}
                        if(!i.아침[1].isNullOrEmpty()) {binding.tvConnerB.text = i.점심[1].joinToString ("\n")}
                    }
                    now.isBefore(dinner) -> {
                        binding.foodtv.text = "아침(18:00~19:30)"
                        if(!i.아침[0].isNullOrEmpty()) {binding.tvConnerA.text = i.저녁[0].joinToString ("\n")}
                    }
                    else -> {
                        binding.foodtv.text = "식당 마감"
                    }
                }
            }
        }
    }

    override fun onClick(v: View) {
        val intent = Intent(
            activity,
            PostListActivity::class.java
        )
        when (v) {
            binding.btn1 -> {
                intent.putExtra("category", "배달")
                startActivity(intent)
            }
            binding.btn2 -> {
                intent.putExtra("category", "택배")
                startActivity(intent)
            }
            binding.btn3 -> {
                intent.putExtra("category", "택시")
                startActivity(intent)
            }
            binding.btn4 -> {
                intent.putExtra("category", "룸메")
                startActivity(intent)
            }
            binding.tvRecentPost -> {
                intent.putExtra("category", "전체 게시물")
                startActivity(intent)
            }
        }
    }

    fun postLoading() {

            CoroutineScope(Dispatchers.Default).launch {

                try {
                    serverAPI.recentPost("home/recentPost/").enqueue(object : Callback<ResultPost> {
                        override fun onResponse(
                            call: Call<ResultPost>,
                            response: Response<ResultPost>
                        ) {
                            modelList.clear()
                            recentRecyclerAdapter.clearList()
                            for (i in response.body()!!.content.indices) {
                                val category = response.body()!!.content[i].category
                                val text = response.body()!!.content[i].title
                                val myModel = RecentModel(category, text)
                                modelList.add(myModel)
                            }
                            recentRecyclerAdapter.submitList(modelList)
                            recentRecyclerAdapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<ResultPost>, t: Throwable) {
                            Toast.makeText(activity, "통신 에러", Toast.LENGTH_SHORT).show()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

}