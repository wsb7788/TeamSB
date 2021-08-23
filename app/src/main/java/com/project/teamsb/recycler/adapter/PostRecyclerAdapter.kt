
package com.project.teamsb.recycler.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding

import com.project.teamsb.recycler.model.PostModel
import com.project.teamsb.recycler.hoder.PostViewHolder

class PostRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<PostModel>()

    private val VIEW_TYPE_POST_LIST = 0
    private val VIEW_TYPE_RECENT_LIST = 0
    private val VIEW_TYPE_LOADING = 1

    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "PostRecyclerAdapter - onCreateViewHolder() called")


        val binding = LayoutRecyclerPostListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)



    }
    override fun getItemCount() = this.modelList.size
    fun getItemContentNo(position: Int) = this.modelList[position].no
    fun submitList(modelList: ArrayList<PostModel>){
        Log.d(TAG, "submitList called")
        this.modelList.addAll(modelList)
    }
    override fun getItemViewType(position: Int): Int {
        return when (modelList[position].title) {
            null -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_POST_LIST
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
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    fun clearList(){
        modelList.clear()
    }


}

