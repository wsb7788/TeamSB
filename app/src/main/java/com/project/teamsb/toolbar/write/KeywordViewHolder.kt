package com.project.teamsb.toolbar.write

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.databinding.LayoutRecyclerKeywordBinding

class KeywordViewHolder(val binding: LayoutRecyclerKeywordBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"

    fun bind(keywordModel: KeywordModel){


        binding.tvInsideRcvKeyword.text = keywordModel.text


    }

}