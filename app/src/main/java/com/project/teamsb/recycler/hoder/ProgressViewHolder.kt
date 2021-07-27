package com.project.teamsb.recycler.hoder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.databinding.LayoutRecyclerProgressBinding

class ProgressViewHolder(val binding: LayoutRecyclerProgressBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"



    init{

        Log.d(TAG, "ProgressViewHolder - init() called")

    }

}