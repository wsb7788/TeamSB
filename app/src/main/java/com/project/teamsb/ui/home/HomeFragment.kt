package com.project.teamsb.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.R
import com.project.teamsb.data.entities.GetMenu
import com.project.teamsb.data.entities.Guide
import com.project.teamsb.data.remote.home.HomeListener
import com.project.teamsb.databinding.FragmentHomeBinding
import com.project.teamsb.main.home.GuideActivity
import com.project.teamsb.main.home.PostListActivity
import com.project.teamsb.post.PostActivity
import com.project.teamsb.recycler.adapter.RecentRecyclerAdapter
import com.project.teamsb.recycler.model.RecentModel
import com.project.teamsb.ui.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random


class HomeFragment : BaseFragment(), View.OnClickListener,RecentRecyclerAdapter.OnItemClickListener, HomeListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()

    var modelList = ArrayList<RecentModel>()

    private lateinit var recentRecyclerAdapter: RecentRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.homeListener = this


        recyclerInit()
        setupViewModel()



        binding.srl.setOnRefreshListener {
            setupViewModel()
            binding.srl.isRefreshing = false
        }
        recentRecyclerAdapter.setItemClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.tvGuidePost.setOnClickListener(this)
        binding.tvRecentPost.setOnClickListener(this)
        return binding.root
    }

    private fun setupViewModel() {
        viewModel.bannerLoading()
        viewModel.calendarLoading()
        viewModel.guideLoading()

    }

    private fun recyclerInit() {
        recentRecyclerAdapter = RecentRecyclerAdapter()
        binding.rcvRecent5.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = recentRecyclerAdapter
        }
        viewModel.liveData.observe(viewLifecycleOwner,{
            recentRecyclerAdapter.submitList(it)
        })
    }


    override fun apiFailure(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    override fun setBanner(topBannerList: List<String>) {
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
        viewModel.recentPostLoading()
        viewModel.calendarLoading()
        viewModel.guideLoading()
    }

    override fun setCalendar(menu: ArrayList<GetMenu>) {
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

    override fun setGuide(content: ArrayList<Guide>) {
        val rand = Random.nextInt(content.size)
        binding.tvGuideTitle.text = content[rand].title
        binding.tvGuideContent.text = content[rand].content
    }

    override fun onClick(v: View?) {
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



    override fun onClick(v: View, position: Int) {
        val intent = Intent(context , PostActivity::class.java)
        intent.putExtra("no", recentRecyclerAdapter.getPostNo(position))
        startActivity(intent)
    }

}