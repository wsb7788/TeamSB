package com.project.teamsb.toolbar

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.R
import com.project.teamsb.api.Notification
import com.project.teamsb.api.ResultNoReturn
import com.project.teamsb.api.ResultNotiList
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivityNotificationBinding
import com.project.teamsb.databinding.ActivitySearchBinding
import com.project.teamsb.post.PostActivity
import com.project.teamsb.recycler.adapter.KeywordRecyclerAdapter
import com.project.teamsb.recycler.adapter.NotificationRecyclerAdapter
import com.project.teamsb.recycler.model.NotificationModel
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

class NotificationActivity:AppCompatActivity(), NotificationRecyclerAdapter.OnItemClickListener {

    val binding by lazy { ActivityNotificationBinding.inflate(layoutInflater) }
    private lateinit var notificationAdapter: NotificationRecyclerAdapter
    var modelList = ArrayList<NotificationModel>()
    var page = 1

    var loadLock = false
    var noMoreItem = false
    var isRefresh = false
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)



        notificationAdapter = NotificationRecyclerAdapter()
        binding.rcvNotification.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity, LinearLayoutManager.VERTICAL, false)
            adapter = notificationAdapter
        }

        binding.rcvNotification.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition > itemTotalCount*0.7) {
                    if (!loadLock&&!noMoreItem) {
                        loadLock = true
                        page++
                        notificationLoading()
                    }
                }
            }
        })
        binding.srl.setOnRefreshListener {
            if (!loadLock) {
                loadLock = true
                page = 1
                isRefresh = true
                notificationLoading()
                noMoreItem = false
            }
            binding.srl.isRefreshing = false
        }

        notificationAdapter.setItemClickListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notification_read_all,menu)
        binding.tvToolbar.text = "알림"
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.read_all ->{

                notificationReadAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun notificationReadAll() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val id = pref.getString("id","")!!
                serverAPI.notiReadAll(id).enqueue(object :
                    Callback<ResultNoReturn> {
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            page = 1
                            notificationLoading()
                        }
                    }
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notificationLoading()
    }

    private fun notificationLoading() {
        binding.progressBar.visibility = VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val id = pref.getString("id","")!!
                serverAPI.notiList(page,id).enqueue(object :
                    Callback<ResultNotiList> {
                    override fun onResponse(call: Call<ResultNotiList>, response: Response<ResultNotiList>) {
                        if(response.body()!!.check){
                            if(page == 1){
                                notificationAdapter.clearList()
                                notificationAdapter.notifyDataSetChanged()
                            }
                            if (response.body()!!.content.size % 10 != 0 || response.body()!!.content.isEmpty()) {
                                noMoreItem = true
                            }
                            setContent(response.body()!!.content)
                            loadLock = false
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    }
                    override fun onFailure(call: Call<ResultNotiList>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun setContent(notification: List<Notification>) {
        modelList.clear()
        for( i in 0..notification.size-1){
            val notiNo = notification[i].notification_no
            val artiNo = notification[i].article_no
            val nickname = notification[i].nickname
            val title = notification[i].title
            val content = notification[i].content
            val checkRead = notification[i].check_read
            val timeCreated = notification[i].timeStamp        // yyyy-MM-dd hh:mm:ss
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
            else{
                timeStamp = timeCreated.substring(5,10)
                timeStamp = timeStamp.replace("-","/")
            }



            var stringProfileImage:String
            if(notification[i].imageSource.isNullOrBlank()){
                stringProfileImage = ""
            }else{
                stringProfileImage = notification[i].imageSource
            }
            val byteProfileImage = Base64.decode(stringProfileImage,0)
            val inputStream = ByteArrayInputStream(byteProfileImage)
            val profileImage = BitmapFactory.decodeStream(inputStream)

            val myModel = NotificationModel(notiNo,artiNo,nickname,title,content,checkRead,timeStamp,profileImage)
            modelList.add(myModel)
        }
        notificationAdapter.submitList(modelList)
        if(page==1){
            notificationAdapter.notifyDataSetChanged()
            isRefresh = false
        }else{
            notificationAdapter.notifyItemRangeInserted(((page-1)* 10),notification.size)
        }
        notificationAdapter.notifyDataSetChanged()



    }

    override fun onClick(v: View, position: Int) {

        val postNo = notificationAdapter.getPostNo(position)!!
        val notiNo = notificationAdapter.getNotiNo(position)!!
        notificationRead(notiNo,postNo)

    }

    private fun notificationRead(notiNo: Int,postNo:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val id = pref.getString("id","")!!
                serverAPI.notiRead(id,notiNo).enqueue(object :
                    Callback<ResultNoReturn> {
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            val intent = Intent(applicationContext,PostActivity::class.java)
                            intent.putExtra("no",postNo)
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

}