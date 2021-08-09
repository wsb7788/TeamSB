package com.project.teamsb.recycler.hoder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.post.App
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.recycler.model.PostModel


class PostViewHolder(val binding: LayoutRecyclerPostListBinding): RecyclerView.ViewHolder(binding.root){


    val TAG: String = "로그"





    fun bind(postModel: PostModel){
        binding.ivNickname.text = postModel.nickname
        binding.tvCategory.text = postModel.category
        binding.tvTitle.text = postModel.title
        binding.tvContent.text = postModel.text
        binding.tvComment.text = postModel.comment
        binding.tvTimeStamp.text = postModel.time

        Glide
            .with(App.instance)
            .load(postModel.profileImage)
            .circleCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.ivProfile)

    }



}