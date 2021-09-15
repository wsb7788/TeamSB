package com.project.teamsb.recycler.hoder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.data.entities.Content
import com.project.teamsb.databinding.LayoutRecyclerRecentBinding
import com.project.teamsb.recycler.model.RecentModel


class RecentViewHolder(val binding: LayoutRecyclerRecentBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"



    init{

        Log.d(TAG, "PostViewHolder - init() called")

    }

    fun bind(recentModel: Content){
        Log.d(TAG, "PostViewHolder - bind() called")

        binding.tvRecentCategory.text = recentModel.category
        binding.tvRecentContent.text = recentModel.title


    }

}