package com.project.teamsb.recycler.hoder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.recycler.model.PostModel


class PostViewHolder(val binding: LayoutRecyclerPostListBinding): RecyclerView.ViewHolder(binding.root){


    val TAG: String = "로그"





    fun bind(postModel: PostModel){

        binding.tvPostTitle.text = postModel.title
        binding.tvPostContent.text = postModel.text
        binding.tvTimeStamp.text = postModel.time
    }



}