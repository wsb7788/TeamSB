
package com.project.teamsb.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.databinding.LayoutRecyclerProgressBinding
import com.project.teamsb.post.ProgressViewHolder

class PostRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<PostModel>()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "PostRecyclerAdapter - onCreateViewHolder() called")
        return when(viewType){
            VIEW_TYPE_ITEM -> {
                val binding = LayoutRecyclerPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding)
            }
            else -> {
                val binding = LayoutRecyclerProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProgressViewHolder(binding)
            }
        }

    }
    override fun getItemCount() = this.modelList.size
    fun submitList(modelList: ArrayList<PostModel>){
        Log.d(TAG, "submitList called")
        this.modelList.addAll(modelList)
        this.modelList.add(PostModel())
    }
    override fun getItemViewType(position: Int): Int {
        // 게시물과 프로그레스바 아이템뷰를 구분할 기준이 필요하다.
        return when (modelList[position].content) {
            null -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }
    fun deleteLoading(){
        Log.d(TAG, "deleteLoading called")
        modelList.removeAt(modelList.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "CommentRecyclerAdapter - onBindViewHolder() called / position: $position")
        if(holder is PostViewHolder)
            holder.bind(this.modelList[position])
    }
}

