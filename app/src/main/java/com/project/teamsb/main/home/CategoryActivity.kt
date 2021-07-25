package com.project.teamsb.main.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.ActivityCategoryBinding
import com.project.teamsb.login.LoginAPI
import com.project.teamsb.post.CommentModel
import com.project.teamsb.post.CommentRecyclerAdapter
import kotlinx.coroutines.*
import org.w3c.dom.Element
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class CategoryActivity: AppCompatActivity() {
    val binding by lazy {ActivityCategoryBinding.inflate(layoutInflater)}

    var modelList = ArrayList<PostModel>()
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter
    var index = 10
    var page = 1
    var isLoading = false

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/home/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var postService: PostAPI = retrofit.create(PostAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d(TAG, "MainActivity - onCreate() called")

        postRecyclerAdapter = PostRecyclerAdapter()

        binding.rcvPost.apply {
            layoutManager = LinearLayoutManager(this@CategoryActivity, LinearLayoutManager.VERTICAL, false)
            adapter = postRecyclerAdapter
        }

        postLoading()

        binding.rcvPost.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.rcvPost.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
                    postLoading()
                }
            }
        })



    }

    private fun postLoading(){
                CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.Default).launch {
                    modelList.clear()

                    Log.d("로그", "코루틴 호출!")
                    val category = intent.getStringExtra("category")!!
                    try {
                                postService.categoryPost(category, page).enqueue(object : Callback<ResultPost>{
                                    override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {
                                        for (i in response.body()!!.content.indices) {
                                            val title = response.body()!!.content[i].title.toString()
                                            val timeStamp = response.body()!!.content[i].timeStamp.toString()
                                            val text = response.body()!!.content[i].text.toString()
                                            val myModel = PostModel(title, text, timeStamp)
                                            modelList.add(myModel)
                                        }
                                        postRecyclerAdapter.submitList(modelList)

                                        postRecyclerAdapter.notifyItemRangeChanged((page*index),index)


                                        page++
                                    }
                                    override fun onFailure(call: Call<ResultPost>, t: Throwable) {
                                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                                    }
                                })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
    }
}