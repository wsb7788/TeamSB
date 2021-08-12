package com.project.teamsb.main.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.api.GetCalendar
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.FragmentHomeBinding
import com.project.teamsb.main.calendar.CalendarObj
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
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentHomeBinding


    var modelList = ArrayList<RecentModel>()
    private lateinit var recentRecyclerAdapter: RecentRecyclerAdapter

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/home/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.tvRecentPost.setOnClickListener(this)
        recentRecyclerAdapter = RecentRecyclerAdapter()

        binding.rcvRecent5.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = recentRecyclerAdapter
        }
        postLoading()

        calendarLoading()
        binding.vf.setInAnimation(context,android.R.anim.fade_in)
        binding.vf.setOutAnimation(context,android.R.anim.fade_out)
        binding.vf.startFlipping()
        binding.vf.flipInterval = 2000

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calendarLoading() {

        CoroutineScope(Dispatchers.IO).launch {
            val current = LocalDateTime.now()
            var formatter = DateTimeFormatter.ISO_DATE
            var formatted = current.format(formatter)
            try{
                CalendarObj.api.getCalendar().enqueue(object :Callback<GetCalendar>{
                    override fun onResponse(call: Call<GetCalendar>, response: Response<GetCalendar>) {
                        for(i in response.body()!!.menu){
                            if(i.일자 == formatted){
                                formatter = DateTimeFormatter.ofPattern("M월 d일 식단표")
                                formatted = current.format(formatter)
                                binding.foodtv.text = formatted
                                binding.tvBreakfast.text  = "[아침]\n" + i.아침[0].joinToString("\n")
                                binding.tvLunch1.text  = "[점심1]\n" + i.점심[0].joinToString("\n")
                                binding.tvLunch2.text  = "[점심2]\n" + i.점심[1].joinToString("\n")
                                binding.tvDinner.text  = "[저녁]\n" + i.저녁[0].joinToString("\n")

                            }
                        }
                    }

                    override fun onFailure(call: Call<GetCalendar>, t: Throwable) {
                    }

                })

            }catch(e: Exception){

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
                    serverAPI.recentPost().enqueue(object : Callback<ResultPost> {
                        override fun onResponse(
                            call: Call<ResultPost>,
                            response: Response<ResultPost>
                        ) {
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