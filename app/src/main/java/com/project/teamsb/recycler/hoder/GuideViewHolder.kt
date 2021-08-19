package com.project.teamsb.recycler.hoder

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerGuideBinding
import com.project.teamsb.databinding.LayoutRecyclerNoticeBinding
import com.project.teamsb.databinding.LayoutRecyclerNotificationBinding
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.post.App
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.recycler.model.GuideModel
import com.project.teamsb.recycler.model.NoticeModel
import com.project.teamsb.recycler.model.PostModel


class GuideViewHolder(val binding: LayoutRecyclerGuideBinding): RecyclerView.ViewHolder(binding.root){


    val TAG: String = "로그"



    var isvisible = false

    fun bind(guideModel: GuideModel){
        binding.tvTitle.text = guideModel.title
        binding.tvSubTitle.text = guideModel.title
        binding.tvSubContent.text = guideModel.content

        binding.clNoticeTitle.setOnClickListener {
            if(!isvisible){
                binding.clNoticeContent.visibility = VISIBLE
                binding.tvSubContent.visibility = VISIBLE
                binding.tvSubTitle.visibility = VISIBLE
                binding.ivArrow.animate().setDuration(50).rotation(180f)
                binding.clMother.setBackgroundColor(Color.parseColor("#e0e0e0"))
                isvisible = true
            }else{
                binding.clNoticeContent.visibility = GONE
                binding.tvSubContent.visibility = GONE
                binding.tvSubTitle.visibility = GONE
                binding.ivArrow.animate().setDuration(50).rotation(0f)
                binding.clMother.setBackgroundColor(Color.parseColor("#ffffff"))
                isvisible = false
            }
        }

    }



}