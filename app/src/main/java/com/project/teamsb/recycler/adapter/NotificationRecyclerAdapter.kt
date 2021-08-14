
package com.project.teamsb.recycler.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.databinding.LayoutRecyclerNotificationBinding
import com.project.teamsb.databinding.LayoutRecyclerProgressBinding
import com.project.teamsb.recycler.model.CommentModel
import com.project.teamsb.recycler.hoder.CommentViewHolder
import com.project.teamsb.recycler.hoder.NotificationViewHolder
import com.project.teamsb.recycler.hoder.ProgressViewHolder
import com.project.teamsb.recycler.model.NotificationModel

class NotificationRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<NotificationModel>()

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = LayoutRecyclerNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NotificationViewHolder(binding)
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
    fun submitList(modelList: ArrayList<NotificationModel>){
        Log.d(TAG, "submitList called")
        this.modelList.addAll(modelList)
//            this.modelList.add(CommentModel())
    }
    override fun getItemViewType(position: Int): Int {
        // 게시물과 프로그레스바 아이템뷰를 구분할 기준이 필요하다.
        return when (modelList[position].nickname) {
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
        if(holder is NotificationViewHolder){
            holder.bind(this.modelList[position])
        }

    }
    fun clearList(){
        modelList.clear()
    }
}

