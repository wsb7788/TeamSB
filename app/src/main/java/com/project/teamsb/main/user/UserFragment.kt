package com.project.teamsb.main.user

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.api.ResultPost
import com.project.teamsb.databinding.FragmentUserBinding
import com.project.teamsb.api.ServerObj
import com.project.teamsb.post.App
import com.project.teamsb.post.PostActivity
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.recycler.model.PostModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class UserFragment : Fragment(),PostRecyclerAdapter.OnItemClickListener {

    lateinit var binding: FragmentUserBinding

    var modelList = ArrayList<PostModel>()
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter

    var page = 1
    var loadLock = false
    var noMoreItem = false
    var isRefresh = false
    lateinit var id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        postRecyclerAdapter = PostRecyclerAdapter()
        postRecyclerAdapter.clearList()
        binding.rcvPost.apply {
            layoutManager = GridLayoutManager(App.instance, 2)
            adapter = postRecyclerAdapter
        }
        id = this!!.requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("id","")!!
        userPostLoading()

        binding.rcvPost.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition == itemTotalCount) {
                    if (!loadLock&&!noMoreItem) {
                        loadLock = true
                        page++
                        userPostLoading()
                    }
                }
            }
        })
        binding.srlCategory.setOnRefreshListener {
            if (!loadLock) {
                loadLock = true
                page = 1
                isRefresh = true
                userPostLoading()
                noMoreItem = false

            }
            binding.srlCategory.isRefreshing = false

        }

        postRecyclerAdapter.setItemClickListener(this)

        return binding.root
    }

    private fun userPostLoading() {
        binding.progressBar.visibility = VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("로그", "코루틴 호출!")
            modelList.clear()
            try {
                ServerObj.api.myArticleList(page,id).enqueue(object : Callback<ResultPost> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {

                        if(page ==1 ){
                            postRecyclerAdapter.clearList()
                        }
                        if (response.body()!!.content.size % 10 != 0 || response.body()!!.content.isEmpty()) {
                            noMoreItem = true
                        }
                        setPost(response)
                        loadLock = false
                        binding.progressBar.visibility = View.INVISIBLE

                    }
                    override fun onFailure(call: Call<ResultPost>, t: Throwable) {
                        Toast.makeText(App.instance, "통신 에러", Toast.LENGTH_SHORT).show()
                        loadLock = false
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun setPost(response: Response<ResultPost>) {
        for (i in response.body()!!.content.indices) {
            val nickname = response.body()!!.content[i].userNickname
            val category = response.body()!!.content[i].category
            val title = response.body()!!.content[i].title
            val text = response.body()!!.content[i].text
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
        if(isRefresh){
            postRecyclerAdapter.notifyDataSetChanged()
            isRefresh = false
        }else{
            postRecyclerAdapter.notifyItemRangeInserted(((page-1)* 10), response.body()!!.content.size)
        }
    }

    override fun onClick(v: View, position: Int) {
        val intent = Intent(App.instance, PostActivity::class.java)
        intent.putExtra("no", postRecyclerAdapter.getItemContentNo(position))
        startActivity(intent)
    }

}