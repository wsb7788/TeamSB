package com.project.teamsb.toolbar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.R
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivitySearchBinding
import com.project.teamsb.databinding.ActivitySettingBinding
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.recycler.model.PostModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity:AppCompatActivity(),View.OnClickListener {
    val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }

    var modelList = ArrayList<PostModel>()
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter

    var index = 10
    var page = 1

    lateinit var category:String

    lateinit var imm: InputMethodManager

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var searchService: ServerAPI = retrofit.create(ServerAPI::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        postRecyclerAdapter = PostRecyclerAdapter()
        initRecycler()
        binding.btnSearch.setOnClickListener(this)
        setSupportActionBar(binding.toolbar)
        category = if(intent.hasExtra("category")) {
            intent.getStringExtra("category")!! }else{
            "all" }

        when(category){
            "all" -> binding.spinner.setSelection(0)
            "delivery"->binding.spinner.setSelection(1)
            "parcel"->binding.spinner.setSelection(2)
            "taxi"->binding.spinner.setSelection(3)
            "laundry"->binding.spinner.setSelection(4)
        }

        binding.rcvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1
                // 스크롤이 끝에 도달했는지 확인
                if (!binding.rcvSearch.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && postRecyclerAdapter.itemCount > 9) {
                    postRecyclerAdapter.deleteLoading()
                    page++
                    postLoading()
                }
            }
        })


    }


    private fun initRecycler() {
        binding.rcvSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = postRecyclerAdapter
        }
    }
    fun postLoading(){
            CoroutineScope(Dispatchers.Default).launch {
                modelList.clear()

                Log.d("로그", "코루틴 호출!")
                try {
                    val keyword = binding.etSearch.text.toString()
                    val category1 = category
                    val page1 = page
                    searchService.search(page1,category1,keyword ).enqueue(object : Callback<ResultPost> {
                        override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {
                            for (i in response.body()!!.content.indices) {
                                val title = response.body()!!.content[i].title.toString()
                                val timeStamp = response.body()!!.content[i].timeStamp.toString()
                                val text = response.body()!!.content[i].text.toString()
                                val myModel = PostModel(title, text, timeStamp)
                                modelList.add(myModel)
                            }
                            postRecyclerAdapter.submitList(modelList)
                            postRecyclerAdapter.notifyItemRangeChanged((page* index), index)

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

    override fun onClick(v: View?) {
        when(v){
            binding.btnSearch ->{
                imm.hideSoftInputFromWindow(v.windowToken,0)
                if (binding.spinner.selectedItemPosition == 0){
                    Toast.makeText(this,"카테고리를 선택해주세요.",Toast.LENGTH_SHORT).show()
                }else{
                    category = when(binding.spinner.selectedItemPosition){
                        1-> "delivery"
                        2-> "parcel"
                        3-> "taxi"
                        4-> "laundry"
                        else-> ""
                    }
                    page = 1
                    postRecyclerAdapter.clearList()
                    postLoading()
                }

            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val category = intent.getStringExtra("category")!!
        when(item.itemId){
            R.id.write_tb -> {
                val intent = Intent(this, WriteActivity::class.java)
                intent.putExtra("category",category)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}