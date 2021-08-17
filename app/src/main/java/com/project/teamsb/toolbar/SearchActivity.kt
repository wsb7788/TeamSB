package com.project.teamsb.toolbar

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.R
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivitySearchBinding
import com.project.teamsb.databinding.DialogEditProfileImageBinding
import com.project.teamsb.databinding.DialogFilterBinding
import com.project.teamsb.post.PostActivity
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
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class SearchActivity:AppCompatActivity(),View.OnClickListener, PostRecyclerAdapter.OnItemClickListener {
    val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }


    private lateinit var view: DialogFilterBinding
    var modelList = ArrayList<PostModel>()
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter


    var index = 10
    var page = 1
    var loadLock = false
    var isRefresh = false
    var noMoreItem = false
    lateinit var category:String

    lateinit var imm: InputMethodManager

    private var filterFocus:Int = 0
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        postRecyclerAdapter = PostRecyclerAdapter()
        initRecycler()
        binding.btnSearch.setOnClickListener(this)
        binding.btnFilter.setOnClickListener(this)
        setSupportActionBar(binding.toolbar)
        category = if(intent.hasExtra("category")) {
            intent.getStringExtra("category")!! }else{
            "all" }

        when(category){
            "all" -> {
                binding.btnFilter.setImageResource(R.drawable.btn_unfiltered_search)
                filterFocus = 0 }
            "배달" -> {
                binding.btnFilter.setImageResource(R.drawable.btn_delivery_search)
                filterFocus = 2 }
            "택배" -> {
                binding.btnFilter.setImageResource(R.drawable.btn_parcel_search)
                filterFocus = 3 }
            "택시" -> {
                binding.btnFilter.setImageResource(R.drawable.btn_taxi_search)
                filterFocus = 4 }
            "룸메" -> {
                binding.btnFilter.setImageResource(R.drawable.btn_room_mate_search)
                filterFocus = 5 }
        }


        binding.srl.setOnRefreshListener {
            if (!loadLock) {
                loadLock = true
                page = 1
                isRefresh = true
                postLoading()
                noMoreItem = false
            }

            binding.srl.isRefreshing = false
        }

        binding.rcvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1

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

        postRecyclerAdapter.setItemClickListener(this)
    }


    private fun initRecycler() {
        binding.rcvSearch.apply {
            layoutManager = GridLayoutManager(this@SearchActivity,  2)
            adapter = postRecyclerAdapter
        }
    }
    fun postLoading(){
        binding.progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Default).launch {
                modelList.clear()

                Log.d("로그", "코루틴 호출!")
                try {
                    val keyword = binding.etSearch.text.toString()
                    val category1 = category
                    val page1 = page
                    serverAPI.search(page1,category1,keyword ).enqueue(object : Callback<ResultPost> {
                        override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {
                            if(page == 1 ){
                                postRecyclerAdapter.clearList()
                            }
                            if (response.body()!!.content.size % 10 != 0 || response.body()!!.content.isEmpty()) {
                                noMoreItem = true
                            }
                            setPost(response)
                            Log.d("로그", "${postRecyclerAdapter.itemCount}")
                            loadLock = false
                            binding.progressBar.visibility = View.INVISIBLE
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
    private fun setPost(response: Response<ResultPost>) {

        if(response.body()!!.content.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "게시글이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            postRecyclerAdapter.notifyDataSetChanged()
        }else{
            for (i in response.body()!!.content.indices) {
                val nickname = response.body()!!.content[i].userNickname
                val category = response.body()!!.content[i].category
                val title = response.body()!!.content[i].title
                val keywords = response.body()!!.content[i].hash


                var stringprofileImage:String
                if(response.body()!!.content[i].imageSource.isNullOrBlank()){
                    stringprofileImage = ""
                }else{
                    stringprofileImage = response.body()!!.content[i].imageSource
                }
                val byteProfileImage = Base64.decode(stringprofileImage,0)
                val inputStream = ByteArrayInputStream(byteProfileImage)
                val profileImage = BitmapFactory.decodeStream(inputStream)




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
                    timeStamp =  (ChronoUnit.SECONDS.between(timeCreatedParsedDateTime, LocalDateTime.now()).toInt() / 60).toString() +"분 전"
                }else if(ChronoUnit.DAYS.between(timeCreatedParsedDate, LocalDate.now()).toInt() == 0)
                    timeStamp = timeCreated.substring(11,16)
                else
                    timeStamp = timeCreated.substring(5,10)

                val comment = response.body()!!.content[i].replyCount
                val no = response.body()!!.content[i].no
                val myModel = PostModel(title,keywords,timeStamp,nickname,comment,category,no,profileImage)
                modelList.add(myModel)
            }
            postRecyclerAdapter.submitList(modelList)
            if(page == 1){
                postRecyclerAdapter.notifyDataSetChanged()
                isRefresh = false
            }else{
                postRecyclerAdapter.notifyItemRangeInserted(((page-1)* index), response.body()!!.content.size)
            }

        }
    }
    override fun onClick(v: View?) {
        when(v){
            binding.btnSearch ->{
                imm.hideSoftInputFromWindow(v.windowToken,0)
                if (filterFocus == 0){
                    Toast.makeText(this,"카테고리를 선택해주세요.",Toast.LENGTH_SHORT).show()
                }else if(binding.etSearch.text.isNullOrBlank()){
                    Toast.makeText(this,"검색할 내용을 입력해주세요.",Toast.LENGTH_SHORT).show()
                }
                else{
                    category = when(filterFocus){
                        2-> "배달"
                        3-> "택배"
                        4-> "택시"
                        5-> "룸메"
                        else-> ""
                    }
                    page = 1
                    loadLock = true
                    postLoading()
                }
            }
            binding.btnFilter->{
                filterDialog()
            }
        }
    }

    private fun filterDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        view = DialogFilterBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.btnFiltered.setOnClickListener{
            filterFocus = 1
            binding.btnFilter.setImageResource(R.drawable.btn_filtered_search)
            alertDialog.hide()
        }
        view.btnDelivery.setOnClickListener{
            filterFocus = 2
            binding.btnFilter.setImageResource(R.drawable.btn_delivery_search)
            alertDialog.hide()
        }
        view.btnParcel.setOnClickListener{
            filterFocus = 3
            binding.btnFilter.setImageResource(R.drawable.btn_parcel_search)
            alertDialog.hide()
        }
        view.btnTaxi.setOnClickListener{
            filterFocus = 4
            binding.btnFilter.setImageResource(R.drawable.btn_taxi_search)
            alertDialog.hide()
        }
        view.btnRoomMate.setOnClickListener{
            filterFocus = 5
            binding.btnFilter.setImageResource(R.drawable.btn_room_mate_search)
            alertDialog.hide()
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
                intent.putExtra("category","all")
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View, position: Int) {
        intent = Intent(this, PostActivity::class.java)
        intent.putExtra("no", postRecyclerAdapter.getItemContentNo(position))
        startActivity(intent)
    }
}