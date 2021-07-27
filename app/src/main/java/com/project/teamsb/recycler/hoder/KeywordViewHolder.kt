package com.project.teamsb.recycler.hoder

import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerKeywordBinding
import com.project.teamsb.recycler.model.KeywordModel

class KeywordViewHolder(val binding: LayoutRecyclerKeywordBinding): RecyclerView.ViewHolder(binding.root) {


    val TAG: String = "로그"

    fun bind(keywordModel: KeywordModel){


        binding.tvInsideRcvKeyword.text = keywordModel.text


    }

}