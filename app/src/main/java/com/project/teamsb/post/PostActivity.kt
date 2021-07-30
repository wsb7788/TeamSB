package com.project.teamsb.post


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
import com.project.teamsb.R
import com.project.teamsb.api.*
import com.project.teamsb.databinding.ActivityPostBinding
import com.project.teamsb.recycler.model.CommentModel
import com.project.teamsb.recycler.adapter.CommentRecyclerAdapter
import com.project.teamsb.toolbar.SearchActivity
import com.project.teamsb.toolbar.WriteActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PostActivity : AppCompatActivity(),View.OnClickListener {

    var TAG: String = "로그"

    private lateinit var binding: ActivityPostBinding

    var modelList = ArrayList<CommentModel>()
    private lateinit var commentRecyclerAdapter: CommentRecyclerAdapter
    var page: Int = 1
    var isUserPost = false
    var LoadLock = false
    var NoMoreItem = false
    lateinit var id:String
    var no: Int = 0
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        no = intent.getIntExtra("no", 0)
        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        id = pref.getString("id","")!!

        checkMod(id, no)
        accessArticle(no)
        commentRecyclerAdapter = CommentRecyclerAdapter()
        binding.rcvComment.apply {
            layoutManager = LinearLayoutManager(this@PostActivity, LinearLayoutManager.VERTICAL, false)
            adapter = commentRecyclerAdapter
        }
        replyLoading(id,page,no)

        binding.postScrollView.viewTreeObserver.addOnScrollChangedListener {
            var view = binding.postScrollView.getChildAt (binding.postScrollView.childCount - 1);
            var diff =(view.bottom - (binding.postScrollView.height + binding.postScrollView.scrollY));
            if (diff == 0) {
                if (!LoadLock) {
                    LoadLock = true
                    if (!NoMoreItem) {
                        replyLoading(id,++page,no)
                    }
                }
            }
        };
        binding.btnComment.setOnClickListener(this)
    }
    private fun checkMod(curUser: String, no: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                serverAPI.checkMod(curUser, no).enqueue(object :
                    Callback<ResultNoReturn>{
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            isUserPost = true
                            invalidateOptionsMenu()
                        }else if(response.body()!!.code == 303){
                            isUserPost = false
                            invalidateOptionsMenu()
                        }
                    }
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun contentLoading(no:Int) {
        CoroutineScope(Dispatchers.Default).async {
            try {
                serverAPI.detail(no).enqueue(object :
                    Callback<ResultPost> {
                    override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {
                        if(response.body()!!.check){
                            setContent(response.body()!!.content[0])
                        }
                    }
                    override fun onFailure(call: Call<ResultPost>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun accessArticle(no:Int) {
        CoroutineScope(Dispatchers.Default).async {
            try {
                serverAPI.accessArticle(no).enqueue(object : Callback<ResultNoReturn>{
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                            contentLoading(no)
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
    private fun replyLoading(id:String, page: Int, no:Int) {
        binding.progressBar.visibility = VISIBLE
        modelList.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        serverAPI.replyList(page, no,id).enqueue(object :
                            Callback<ResultReply> {
                            override fun onResponse(call: Call<ResultReply>, response: Response<ResultReply>) {
                                for (i in response.body()!!.content.indices) {
                                    if (response.body()!!.content.size % 20 != 0 || response.body()!!.content.isEmpty()) {
                                        NoMoreItem = true;
                                    }
                                    val nickname = response.body()!!.content[i].userNickname
                                    val content = response.body()!!.content[i].content
                                    val myModel = CommentModel(name = nickname, content = content)
                                    modelList.add(myModel)
                                }
                                commentRecyclerAdapter.submitList(modelList)
                                commentRecyclerAdapter.notifyDataSetChanged()
                                LoadLock = false
                                binding.progressBar.visibility = INVISIBLE
                            }
                            override fun onFailure(call: Call<ResultReply>, t: Throwable) {
                                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                                LoadLock = false
                                binding.progressBar.visibility = INVISIBLE
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

    }
    override fun onClick(v: View?) {
        when(v){
            binding.btnComment ->{
                var text = binding.etComment.text.toString()
                text.replace(" ", "")
                if(text.isNullOrBlank()){
                    Toast.makeText(this,"댓글을 입력하세요.",Toast.LENGTH_SHORT).show()
                }else{
                    uploadComment(no,id)
                }
            }

        }
    }
    private fun uploadComment(no:Int, curUser:String) {
        runBlocking {
                CoroutineScope(Dispatchers.Default).async {
                    modelList.clear()
                    val content = binding.etComment.text.toString()
                    try {
                        serverAPI.writeComment(no,content,curUser).enqueue(object :
                            Callback<ResultNoReturn>{
                            override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                                Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                binding.etComment.text.clear()
                            }
                            override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.await()
            }
        }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isUserPost){
            menuInflater.inflate(R.menu.menu_edit,menu)
            menuInflater.inflate(R.menu.menu_delete,menu)
        }else{
            menuInflater.inflate(R.menu.menu_report,menu)
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_tb -> {
                intent = Intent(this, WriteActivity::class.java)
                intent.putExtra("no",no)
                intent.putExtra("edit",true)
                intent.putExtra("category",binding.tvCategory2.text)
                startActivity(intent)
            }
            R.id.report_tb -> {

            }
            R.id.delete_tb -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun setContent(content: Content){
        binding.tvTitle2.text = content.title
        binding.tvCategory2.text = when(content.category){
            "delivery"->"배달"
            "parcel"->"택배"
            "taxi"->"택시"
            "laundry"->"빨래"
            else -> "그럴리가업썽"
        }
        binding.tvTime2.text = content.timeStamp
        binding.tvWriter2.text = content.userNickname
        var hash = ""
        if(content.hash.isNotEmpty()){
            for(i in content.hash){
                hash += " #"
                hash += i
            }
        }
        binding.tvKeyword2.text = hash
        binding.tvContent.text = content.text
    }

}
