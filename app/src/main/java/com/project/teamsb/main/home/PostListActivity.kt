package com.project.teamsb.main.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
                if (lastVisibleItemPosition > itemTotalCount * 0.7) {
                    if (!loadLock) {
                        loadLock = true
                        if (!noMoreItem) {
                            postRecyclerAdapter.deleteLoading()
                            page++
                            postLoading()
                        }
                    }

                }
            }
        })
        binding.srlCategory.setOnRefreshListener {
            page = 1
            postRecyclerAdapter.clearList()
            isRefresh = true
            postLoading()
            binding.srlCategory.isRefreshing = false
        }
        postRecyclerAdapter.setItemClickListener(this)

    }

    private fun setToolBarText() {
        category = if(intent.hasExtra("category")) {
            intent.getStringExtra("category")!! }else{
            "all" }
        categoryQuery = when(category){
            "택배"->"parcel"
            "배달"->"delivery"
            "택시"->"taxi"
            "빨래"->"laundry"
            else ->"ㄴㄴ"
        }

        binding.tvToolbar.text = category
    }

    private fun initRecycler() {
        binding.rcvPost.apply {
            layoutManager = LinearLayoutManager(this@PostListActivity, LinearLayoutManager.VERTICAL, false)
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

                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("로그", "코루틴 호출!")
                    modelList.clear()
                    try {
                                serverAPI.categoryPost(categoryQuery, page).enqueue(object : Callback<ResultPost>{
                                    override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {
                                        if (response.body()!!.content.size % 20 != 0 || response.body()!!.content.isEmpty()) {
                                            noMoreItem = true
                                        }
                                        for (i in response.body()!!.content.indices) {
                                            val title = response.body()!!.content[i].title
                                            val text = response.body()!!.content[i].text
                                            val timeStamp = response.body()!!.content[i].timeStamp
                                            val no = response.body()!!.content[i].no
                                            val myModel = PostModel(title, text, timeStamp, no)
                                            modelList.add(myModel)
                                        }
                                        postRecyclerAdapter.submitList(modelList)
                                        if(isRefresh){
                                            postRecyclerAdapter.notifyDataSetChanged()
                                            isRefresh = false
                                        }else{
                                            postRecyclerAdapter.notifyItemRangeInserted(((page-1)* index), index)
                                        }
                                        loadLock = false
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

