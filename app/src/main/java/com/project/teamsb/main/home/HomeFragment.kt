package com.project.teamsb.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.FragmentHomeBinding
import com.project.teamsb.recycler.model.RecentModel
import com.project.teamsb.recycler.adapter.RecentRecyclerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentHomeBinding


    var modelList = ArrayList<RecentModel>()
    private lateinit var recentRecyclerAdapter: RecentRecyclerAdapter

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/home/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.tvRecentPost.setOnClickListener(this)
        recentRecyclerAdapter = RecentRecyclerAdapter()

        binding.rcvRecent5.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = recentRecyclerAdapter
        }
        postLoading()


        return binding.root
    }

    override fun onClick(v: View) {
        val intent = Intent(
            activity,
            PostListActivity::class.java
        )
        when (v) {
            binding.btn1 -> {
                intent.putExtra("category", "delivery")
                startActivity(intent)
            }
            binding.btn2 -> {
                intent.putExtra("category", "parcel")
                startActivity(intent)
            }
            binding.btn3 -> {
                intent.putExtra("category", "taxi")
                startActivity(intent)
            }
            binding.btn4 -> {
                intent.putExtra("category", "laundry")
                startActivity(intent)
            }
            binding.tvRecentPost -> {
                intent.putExtra("category", "all")
                startActivity(intent)
            }
        }
    }

    fun postLoading() {

            CoroutineScope(Dispatchers.Default).launch {

                try {
                    serverAPI.recentPost().enqueue(object : Callback<ResultPost> {
                        override fun onResponse(
                            call: Call<ResultPost>,
                            response: Response<ResultPost>
                        ) {
                            for (i in response.body()!!.content.indices) {
                                val category = when(response.body()!!.content[i].category){
                                    "laundry" -> "빨래"
                                    "parcel" -> "택배"
                                    "taxi" -> "택시"
                                    "delivery" -> "배달"
                                    else -> "그럴리가.."
                                }
                                val text = response.body()!!.content[i].text.toString()
                                val myModel = RecentModel(category, text)
                                modelList.add(myModel)
                            }
                            recentRecyclerAdapter.submitList(modelList)
                            recentRecyclerAdapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<ResultPost>, t: Throwable) {
                            Toast.makeText(activity, "통신 에러", Toast.LENGTH_SHORT).show()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

}