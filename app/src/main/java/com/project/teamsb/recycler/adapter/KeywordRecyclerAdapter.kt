
package com.project.teamsb.recycler.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerKeywordBinding
import com.project.teamsb.recycler.model.KeywordModel
import com.project.teamsb.recycler.hoder.KeywordViewHolder

class KeywordRecyclerAdapter: RecyclerView.Adapter<KeywordViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<KeywordModel>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        Log.d(TAG, "CommentRecyclerAdapter - onCreateViewHolder() called")

        val binding = LayoutRecyclerKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordViewHolder(binding)



    }
    override fun getItemCount() = this.modelList.size
    fun submitList(modelList: ArrayList<KeywordModel>){
        this.modelList.addAll(modelList)
    }
    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {

            holder.bind(this.modelList[position])

    }
}

