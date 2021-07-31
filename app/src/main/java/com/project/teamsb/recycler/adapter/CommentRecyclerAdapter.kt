
package com.project.teamsb.recycler.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.databinding.LayoutRecyclerProgressBinding
import com.project.teamsb.recycler.model.CommentModel
import com.project.teamsb.recycler.hoder.CommentViewHolder
import com.project.teamsb.recycler.hoder.ProgressViewHolder

class CommentRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<CommentModel>()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "CommentRecyclerAdapter - onCreateViewHolder() called")
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = LayoutRecyclerCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CommentViewHolder(binding)
            }
            else -> {
                val binding = LayoutRecyclerProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProgressViewHolder(binding)
            }
        }
        /* val binding = LayoutRecyclerCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return CommentViewHolder(binding)*/
    }
    override fun getItemCount() = this.modelList.size
    fun submitList(modelList: ArrayList<CommentModel>){
        Log.d(TAG, "submitList called")
        this.modelList.addAll(modelList)
//            this.modelList.add(CommentModel())
    }
    override fun getItemViewType(position: Int): Int {
        // 게시물과 프로그레스바 아이템뷰를 구분할 기준이 필요하다.
        return when (modelList[position].name) {
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
        if(holder is CommentViewHolder){
            holder.bind(this.modelList[position])
        }

    }
}

