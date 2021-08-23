package com.project.teamsb.main.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.api.Guide
import com.project.teamsb.api.ResultGuide
import com.project.teamsb.databinding.ActivityGuideBinding
import com.project.teamsb.api.ServerObj
import com.project.teamsb.recycler.adapter.GuideRecyclerAdapter
import com.project.teamsb.recycler.model.GuideModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class GuideActivity : AppCompatActivity(){
    val binding by lazy { ActivityGuideBinding.inflate(layoutInflater)}

    lateinit var guideRecyclerAdapter: GuideRecyclerAdapter

    var modelList = ArrayList<GuideModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        guideRecyclerAdapter = GuideRecyclerAdapter()

        binding.rcv.apply {
            layoutManager = LinearLayoutManager(this@GuideActivity, LinearLayoutManager.VERTICAL, false)
            adapter = guideRecyclerAdapter
        }
        guideLoading()

    }

    private fun guideLoading() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ServerObj.api.guideList().enqueue(object:retrofit2.Callback<ResultGuide>{
                    override fun onResponse(call: Call<ResultGuide>, response: Response<ResultGuide>) {
                        if (response.body()!!.code==200){
                            setContent(response.body()!!.content)
                        }
                    }
                    override fun onFailure(call: Call<ResultGuide>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    private fun setContent(content: ArrayList<Guide>) {
        for (i in 0 until content.size){
            val title = content[i].title
            val text = content[i].content
            val myModel = GuideModel(title,text)
            modelList.add(myModel)
        }
        guideRecyclerAdapter.submitList(modelList)
        guideRecyclerAdapter.notifyDataSetChanged()
    }

}