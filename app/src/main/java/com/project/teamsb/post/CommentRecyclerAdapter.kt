package com.project.teamsb.post

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding

class CommentRecyclerAdapter: RecyclerView.Adapter<CommentViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<CommentModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        Log.d(TAG, "CommentRecyclerAdapter - onCreateViewHolder() called")
        val binding = LayoutRecyclerCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        Log.d(TAG, "CommentRecyclerAdapter - onBindViewHolder() called / position: $position")
        holder.bind(this.modelList[position])
    }

    override fun getItemCount() = this.modelList.size

    fun submitList(modelList: ArrayList<CommentModel>){
        Log.d(TAG, "submitList called")
        this.modelList = modelList
    }
}

