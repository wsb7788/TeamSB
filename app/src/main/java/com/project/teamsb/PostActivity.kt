package com.project.teamsb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.databinding.ActivityPostBinding


class PostActivity:AppCompatActivity() {

    var TAG: String = "로그"

    private lateinit var binding: ActivityPostBinding

    var modelList = ArrayList<CommentModel>()

    private lateinit var commentRecyclerAdapter: CommentRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "MainActivity - onCreate() called")
        Log.d(TAG, "MainActivity - this.modelList.size: ${this.modelList.size}")

        for(i in 1..10){
            val myModel = CommentModel(name = "원승빈 $i", profileImage = R.drawable.profile_image, content = "앙기모띠")

            this.modelList.add(myModel)
        }

        commentRecyclerAdapter = CommentRecyclerAdapter()

        commentRecyclerAdapter.submitList(this.modelList)

        binding.rcvComment.apply {
            layoutManager = LinearLayoutManager(this@PostActivity, LinearLayoutManager.VERTICAL, false)

            adapter = commentRecyclerAdapter
        }

    }
}