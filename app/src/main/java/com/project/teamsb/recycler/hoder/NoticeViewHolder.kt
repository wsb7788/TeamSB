package com.project.teamsb.recycler.hoder

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerNoticeBinding
import com.project.teamsb.databinding.LayoutRecyclerNotificationBinding
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.post.App
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.recycler.model.NoticeModel
import com.project.teamsb.recycler.model.PostModel


class NoticeViewHolder(val binding: LayoutRecyclerNoticeBinding): RecyclerView.ViewHolder(binding.root){


    val TAG: String = "로그"



    var isvisible = false

    fun bind(noticeModel: NoticeModel){
        binding.tvTitle.text = noticeModel.title
        binding.tvSubTitle.text = noticeModel.title
        binding.tvSubContent.text = noticeModel.content
        binding.ivTimeStamp.text = noticeModel.timeStamp
        if (noticeModel.fixTop!!){
            binding.ivFixed.visibility = VISIBLE
        }else{
            binding.ivFixed.visibility = INVISIBLE
        }
        binding.clNoticeTitle.setOnClickListener {
            if(!isvisible){
                binding.clNoticeContent.visibility = VISIBLE
                binding.clMother.setBackgroundColor(Color.parseColor("#e0e0e0"))
                isvisible = true
            }else{
                binding.clNoticeContent.visibility = GONE
                binding.clMother.setBackgroundColor(Color.parseColor("#ffffff"))
                isvisible = false
            }

        }

    }



}