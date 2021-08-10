package com.project.teamsb.recycler.hoder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.post.App
import com.project.teamsb.recycler.model.CommentModel

class CommentViewHolder(val binding: LayoutRecyclerCommentBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"



    init{

        Log.d(TAG, "CommentViewHolder - init() called")

    }

    fun bind(commentModel: CommentModel){
        Log.d(TAG, "CommentViewHolder - bind() called")

        binding.tvCommentNickname.text = commentModel.nickname
        binding.tvCommentContent.text = commentModel.content
        if(commentModel.id!!){
            binding.tvCommentNickname.setTextColor(-0x62588)
        }
        Glide
            .with(App.instance)
            .load(commentModel.profileImage)
            .circleCrop()
            .placeholder(R.drawable.profile_basic)
            .into(binding.ivCommentProfile)

        binding.tvCommentTimeStamp.text = commentModel.timeStamp
    }

}