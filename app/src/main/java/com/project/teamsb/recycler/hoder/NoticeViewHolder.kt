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





    fun bind(noticeModel: NoticeModel){
        binding.tvTitle.text = noticeModel.title
        binding.tvSubTitle.text = noticeModel.title
        binding.tvSubContent.text = noticeModel.content
        binding.ivTimeStamp.text = noticeModel.timeStamp
        if (noticeModel.realTop!!){
            binding.ivFixed.visibility = VISIBLE
        }else{
            binding.ivFixed.visibility = INVISIBLE
        }
        if(noticeModel.isVisible!!){
            binding.clNoticeContent.visibility = VISIBLE
            binding.tvSubContent.visibility = VISIBLE
            binding.tvSubTitle.visibility = VISIBLE
            binding.ivArrow.animate().setDuration(50).rotation(180f)
            binding.clMother.setBackgroundColor(Color.parseColor("#e0e0e0"))
        }else{
            binding.clNoticeContent.visibility = GONE
            binding.tvSubContent.visibility = GONE
            binding.tvSubTitle.visibility = GONE
            binding.ivArrow.animate().setDuration(50).rotation(0f)
            binding.clMother.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        /*binding.clNoticeTitle.setOnClickListener {
            if(!noticeModel.isVisible){
                binding.clNoticeContent.visibility = VISIBLE
                binding.tvSubContent.visibility = VISIBLE
                binding.tvSubTitle.visibility = VISIBLE
                binding.ivArrow.animate().setDuration(50).rotation(180f)
                binding.clMother.setBackgroundColor(Color.parseColor("#e0e0e0"))
                this.isvisible = true
            }else{
                binding.clNoticeContent.visibility = GONE
                binding.tvSubContent.visibility = GONE
                binding.tvSubTitle.visibility = GONE
                binding.ivArrow.animate().setDuration(50).rotation(0f)
                binding.clMother.setBackgroundColor(Color.parseColor("#ffffff"))
                this.isvisible = false
            }
        }*/

    }



}