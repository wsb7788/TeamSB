package com.project.teamsb.recycler.hoder

import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.databinding.LayoutTopBannerBinding
import com.project.teamsb.post.App
import com.project.teamsb.recycler.model.CommentModel
import com.project.teamsb.recycler.model.TopBannerModel

class TopBannerViewHolder(val binding: LayoutTopBannerBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"



    init{

        Log.d(TAG, "CommentViewHolder - init() called")

    }

    fun bind(topBannerModel: TopBannerModel){
        binding.tvBanner.text = topBannerModel.text
    }
}