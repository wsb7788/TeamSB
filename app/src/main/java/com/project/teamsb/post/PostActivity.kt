package com.project.teamsb.post


import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ResultReply
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivityPostBinding
import com.project.teamsb.recycler.model.CommentModel
import com.project.teamsb.recycler.adapter.CommentRecyclerAdapter
import com.project.teamsb.recycler.model.PostModel
import kotlinx.coroutines.*
import org.w3c.dom.Element
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class PostActivity : AppCompatActivity() {

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

    var contentLoadService: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "MainActivity - onCreate() called")


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



    }

    private fun contentLoading() {
        runBlocking {
            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.Default).async {
                    val no = intent.getIntExtra("no", 0)!!
                    try {
                        contentLoadService.detail(no).enqueue(object :
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
                }.await()
            }
        }
    }

    private fun replyLoading() {
        runBlocking{
            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.Default).async {
                    modelList.clear()
                    var page = page
                    var article_no =  53
                    //var article_no =  intent.getIntExtra("no", 0)!!
                    var curUser = "wsb7788"
                    try {

                        contentLoadService.replyList(page, article_no,curUser).enqueue(object :
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
                                commentRecyclerAdapter.notifyItemRangeChanged((page* index), index)

                            }

                            override fun onFailure(call: Call<ResultReply>, t: Throwable) {
                                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.await()
                isLoading = false
            }
        }

    }
}
