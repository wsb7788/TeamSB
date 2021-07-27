package com.project.teamsb.recycler.hoder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.recycler.model.PostModel


class PostViewHolder(val binding: LayoutRecyclerPostListBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"





    fun bind(postModel: PostModel){

        binding.tvPostTitle.text = postModel.title
        binding.tvPostContent.text = postModel.content
        binding.tvTimeStamp.text = postModel.time

    }

}