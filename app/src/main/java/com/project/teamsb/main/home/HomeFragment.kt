package com.project.teamsb.main.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.R
import com.project.teamsb.api.*
import com.project.teamsb.databinding.FragmentHomeBinding
import com.project.teamsb.api.ServerObj
import com.project.teamsb.recycler.model.RecentModel
import com.project.teamsb.recycler.adapter.RecentRecyclerAdapter
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
import kotlin.random.Random


class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentHomeBinding



    var modelList = ArrayList<RecentModel>()

    private lateinit var recentRecyclerAdapter: RecentRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.tvGuidePost.setOnClickListener(this)
        binding.tvRecentPost.setOnClickListener(this)
        recentRecyclerAdapter = RecentRecyclerAdapter()

        binding.rcvRecent5.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = recentRecyclerAdapter
        }

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
                ServerObj.api.topBanner().enqueue(object: Callback<ResponseBanner>{
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
        var layoutParam = ViewGroup.LayoutParams(0,0)
        layoutParam.width = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParam.height = LinearLayout.LayoutParams.WRAP_CONTENT
        for(i in topBannerList.indices){
            var text = TextView(context)
            text.setTextColor(Color.parseColor("#000000"))
            text.text = topBannerList[i]
            text.textSize = 15F
            binding.vf.addView(text,layoutParam)
        }
        binding.vf.setInAnimation(context,R.anim.slide_in)
        binding.vf.setOutAnimation(context,R.anim.slide_out)
        binding.vf.startFlipping()
        binding.vf.flipInterval = 2000

    }

    override fun onResume() {
        super.onResume()
        postLoading()
        calendarLoading()
        guideLoading()
    }

    private fun guideLoading() {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                ServerObj.api.guideList().enqueue(object :Callback<ResultGuide>{
                    override fun onResponse(call: Call<ResultGuide>, response: Response<ResultGuide>) {
                        val rand = Random.nextInt(response.body()!!.content.size)
                        binding.tvGuideTitle.text = response.body()!!.content[rand].title
                        binding.tvGuideContent.text = response.body()!!.content[rand].content
                    }
                    override fun onFailure(call: Call<ResultGuide>, t: Throwable) {
                    }
                })
            }catch(e: Exception){

            }

        }
    }


    private fun calendarLoading() {

        CoroutineScope(Dispatchers.IO).launch {
            try{
                ServerObj.api.getCalendar().enqueue(object :Callback<GetCalendar>{
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
            binding.tvGuidePost -> {
                val intent = Intent(activity, GuideActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun postLoading() {

            CoroutineScope(Dispatchers.Default).launch {

                try {
                    ServerObj.api.recentPost("home/recentPost/").enqueue(object : Callback<ResultPost> {
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