package com.project.teamsb.main.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.R
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivityCategoryBinding
import com.project.teamsb.post.PostActivity
import com.project.teamsb.recycler.model.PostModel
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.toolbar.SearchActivity
import com.project.teamsb.toolbar.WriteActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList


class PostListActivity: AppCompatActivity(),PostRecyclerAdapter.OnItemClickListener {
    val binding by lazy {ActivityCategoryBinding.inflate(layoutInflater)}

    var modelList = ArrayList<PostModel>()
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter

    lateinit var category: String
    lateinit var categoryQuery: String
    var index = 10
    var page = 1
    var loadLock = false
    var noMoreItem = false
    var isRefresh = false
    var returnWrite = false
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/home/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setToolBarText()


        category = intent.getStringExtra("category")!!
        Log.d(TAG, "MainActivity - onCreate() called")

        postRecyclerAdapter = PostRecyclerAdapter()

        initRecycler()
        postLoading()

        binding.rcvPost.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition > itemTotalCount*0.7) {
                    if (!loadLock) {
                        loadLock = true
                        if (!noMoreItem) {
                            page++
                            postLoading()
                        }
                    }
                }
            }
        })
        binding.srlCategory.setOnRefreshListener {
            if (!loadLock) {
                loadLock = true
                page = 1
                postRecyclerAdapter.clearList()
                isRefresh = true
                postLoading()
                noMoreItem = false
            }

            binding.srlCategory.isRefreshing = false
        }
        postRecyclerAdapter.setItemClickListener(this)

    }
    override fun onResume() {
        super.onResume()
        if(returnWrite){
            page = 1
            postRecyclerAdapter.clearList()
            isRefresh = true
            postLoading()
            noMoreItem = false
            returnWrite = false
        }

    }

    private fun setToolBarText() {
        category = if(intent.hasExtra("category")) {
            intent.getStringExtra("category")!! }else{
            "all" }
        categoryQuery = when(category){
            "택배"->"parcel"
            "배달"->"delivery"
            "택시"->"taxi"
            "룸메이트"->"room-mate"
            else ->"all"
        }

        binding.tvToolbar.text = category
    }

    private fun initRecycler() {
        binding.rcvPost.apply {
            layoutManager = GridLayoutManager(this@PostListActivity, 2)
            adapter = postRecyclerAdapter
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        menuInflater.inflate(R.menu.menu_write,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.write_tb -> {
                val intent = Intent(this, WriteActivity::class.java)
                intent.putExtra("category",category)
                startActivity(intent)
                returnWrite = true
            }
            R.id.search_tb -> {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("category", category)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun postLoading(){
        binding.progressBar.visibility = VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("로그", "코루틴 호출!")
                    modelList.clear()
                    try {
                                serverAPI.categoryPost(categoryQuery, page).enqueue(object : Callback<ResultPost>{
                                    @RequiresApi(Build.VERSION_CODES.O)
                                    override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {

                                        if (response.body()!!.content.size % 10 != 0 || response.body()!!.content.isEmpty()) {
                                            noMoreItem = true
                                        }
                                        for (i in response.body()!!.content.indices) {
                                            val nickname = response.body()!!.content[i].userNickname
                                            val category = response.body()!!.content[i].category
                                            val title = response.body()!!.content[i].title
                                            val text = response.body()!!.content[i].text
                                            val timeCreated = response.body()!!.content[i].timeStamp        // yyyy-MM-dd hh:mm:ss
                                            var timeStamp = ""
                                            val y = timeCreated.substring(0,4).toInt()
                                            val M = timeCreated.substring(5,7).toInt()
                                            val d = timeCreated.substring(8,10).toInt()
                                            val h = timeCreated.substring(11,13).toInt()
                                            val m = timeCreated.substring(14,16).toInt()
                                            val s = timeCreated.substring(17,19).toInt()
                                            var timeCreatedParsedDateTime = LocalDateTime.of(y,M,d,h,m,s)
                                            var timeCreatedParsedDate = LocalDate.of(y,M,d)

                                            if(ChronoUnit.HOURS.between(LocalDateTime.now(),timeCreatedParsedDateTime).toInt() ==0){
                                                timeStamp =  (ChronoUnit.SECONDS.between(timeCreatedParsedDateTime,LocalDateTime.now()).toInt() / 60).toString() +"분 전"
                                            }else if(ChronoUnit.DAYS.between(timeCreatedParsedDate,LocalDate.now()).toInt() == 0)
                                                timeStamp = timeCreated.substring(11,16)
                                            else
                                                timeStamp = timeCreated.substring(5,10)

                                            val comment = response.body()!!.content[i].replyCount
                                            val no = response.body()!!.content[i].no
                                            val myModel = PostModel(title,text,timeStamp,nickname,comment,category,no)
                                            modelList.add(myModel)
                                        }
                                        postRecyclerAdapter.submitList(modelList)
                                        if(isRefresh){
                                            postRecyclerAdapter.notifyDataSetChanged()
                                            isRefresh = false
                                        }else{
                                            postRecyclerAdapter.notifyItemRangeInserted(((page-1)* index), response.body()!!.content.size)
                                        }
                                        Log.d("로그", "${postRecyclerAdapter.itemCount}")
                                        loadLock = false
                                        binding.progressBar.visibility = INVISIBLE

                                    }
                                    override fun onFailure(call: Call<ResultPost>, t: Throwable) {
                                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                                        loadLock = false
                                    }
                                })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

    }

    override fun onClick(v: View, position: Int) {
        intent = Intent(this, PostActivity::class.java)
        intent.putExtra("no", postRecyclerAdapter.getItemContentNo(position))
        startActivity(intent)
    }
}

