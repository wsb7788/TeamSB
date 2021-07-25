package com.project.teamsb.main.home

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding


class PostViewHolder(val binding: LayoutRecyclerPostListBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"



    init{

        Log.d(TAG, "PostViewHolder - init() called")

    }

    fun bind(postModel: PostModel){
        Log.d(TAG, "PostViewHolder - bind() called")

        binding.tvPostTitle.text = postModel.title
        binding.tvPostContent.text = postModel.content
        binding.tvTimeStamp.text = postModel.time

    }

}