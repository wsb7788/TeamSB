
package com.project.teamsb.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.databinding.LayoutRecyclerProgressBinding

class PostRecyclerAdapter: RecyclerView.Adapter<PostViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<PostModel>()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        Log.d(TAG, "PostRecyclerAdapter - onCreateViewHolder() called")

        val binding = LayoutRecyclerPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)

    }
    override fun getItemCount() = this.modelList.size
    fun submitList(modelList: ArrayList<PostModel>){
        Log.d(TAG, "submitList called")
        this.modelList.addAll(modelList)
    }
    override fun getItemViewType(position: Int): Int {
        // 게시물과 프로그레스바 아이템뷰를 구분할 기준이 필요하다.
        return when (modelList[position].title) {
            null -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }
    fun deleteLoading(){
        Log.d(TAG, "deleteLoading called")
        modelList.removeAt(modelList.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Log.d(TAG, "CommentRecyclerAdapter - onBindViewHolder() called / position: $position")
            holder.bind(this.modelList[position])
    }
}

