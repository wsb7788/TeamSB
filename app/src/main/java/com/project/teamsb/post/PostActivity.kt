package com.project.teamsb.post


import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.api.ResultNoReturn
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ResultReply
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivityPostBinding
import com.project.teamsb.recycler.model.CommentModel
import com.project.teamsb.recycler.adapter.CommentRecyclerAdapter
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
    var index: Int = 20
    var page: Int = 1
    var isLoading = false

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var contentService: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentLoading()
        commentRecyclerAdapter = CommentRecyclerAdapter()
        binding.rcvComment.apply {
            layoutManager = LinearLayoutManager(this@PostActivity, LinearLayoutManager.VERTICAL, false)
            adapter = commentRecyclerAdapter
        }
        replyLoading()

        binding.postScrollView.viewTreeObserver.addOnScrollChangedListener {
            var view = binding.postScrollView.getChildAt (binding.postScrollView.childCount - 1);
            var diff =(view.bottom - (binding.postScrollView.height + binding.postScrollView.scrollY));

            if (diff == 0) {
                if(!isLoading){
                    isLoading = true
                    Log.d(TAG, "로딩 리스너 호출!")
                    replyLoading()
                    page++
                }
            }
        };

        binding.btnComment.setOnClickListener(this)

    }

    private fun contentLoading() {

                CoroutineScope(Dispatchers.Default).async {
                    val no = intent.getIntExtra("no", 0)!!
                    try {
                        contentService.accessArticle(no).enqueue(object :
                            Callback<ResultNoReturn>{
                            override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                                Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                            }
                            override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                            }
                        })
                        contentService.detail(no).enqueue(object :
                            Callback<ResultPost> {
                            override fun onResponse(
                                call: Call<ResultPost>,
                                response: Response<ResultPost>
                            ) {
                                binding.tvTitle2.text = response.body()!!.content[0].title
                                binding.tvCategory2.text = response.body()!!.content[0].category
                                binding.tvTime2.text = response.body()!!.content[0].timeStamp
                                binding.tvWriter2.text = response.body()!!.content[0].userNickname
                                binding.tvKeyword2.text = response.body()!!.content[0].hash[0]
                                binding.tvContent.text = response.body()!!.content[0].text
                            }

                            override fun onFailure(call: Call<ResultPost>, t: Throwable) {
                                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


    }

    private fun replyLoading() {
        runBlocking{
            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.Default).async {
                    modelList.clear()
                    var page = page
                    var article_no =  intent.getIntExtra("no", 0)
                    //var article_no =
                    var curUser = "wsb7788"
                    try {

                        contentService.replyList(page, article_no,curUser).enqueue(object :
                            Callback<ResultReply> {
                            override fun onResponse(call: Call<ResultReply>, response: Response<ResultReply>) {
                                for (i in response.body()!!.content.indices) {
                                    val nickname = response.body()!!.content[i].userNickname
                                    //val img = response.body()!!.content[i].userNickname
                                    val content = response.body()!!.content[i].content

                                    val myModel = CommentModel(name = nickname, content = content)

                                    modelList.add(myModel)
                                }
                                commentRecyclerAdapter.submitList(modelList)
                                //commentRecyclerAdapter.notifyItemRangeChanged((page* index), index)
                                commentRecyclerAdapter.notifyDataSetChanged()
                               // binding.progressBar.visibility = INVISIBLE

                            }

                            override fun onFailure(call: Call<ResultReply>, t: Throwable) {
                                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.await()

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
                    uploadComment()

                }
            }

        }
    }

    private fun uploadComment() {
        runBlocking {

                CoroutineScope(Dispatchers.Default).async {
                    modelList.clear()
                    val no = intent.getIntExtra("no",0)
                    val content = binding.etComment.text.toString()
                    val curUser = "wsb7788"
                    try {
                        contentService.writeComment(no,content,curUser).enqueue(object :
                            Callback<ResultNoReturn>{
                            override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                                Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                                /*val myModel = CommentModel(name = curUser, content = content)
                                modelList.add(myModel)
                                commentRecyclerAdapter.submitList(modelList)
                                commentRecyclerAdapter.notifyDataSetChanged()*/
                                replyLoading()
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

}
