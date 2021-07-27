package com.project.teamsb.main.home

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.databinding.LayoutRecyclerRecentBinding


class RecentViewHolder(val binding: LayoutRecyclerRecentBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"



    init{

        Log.d(TAG, "PostViewHolder - init() called")

    }

    fun bind(recentModel: RecentModel){
        Log.d(TAG, "PostViewHolder - bind() called")

        binding.tvRecentCategory.text = recentModel.category
        binding.tvRecentContent.text = recentModel.content


    }

}