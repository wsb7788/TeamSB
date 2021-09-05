
package com.project.teamsb.recycler.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.data.entities.Content
import com.project.teamsb.databinding.LayoutRecyclerRecentBinding
import com.project.teamsb.recycler.model.RecentModel
import com.project.teamsb.recycler.hoder.RecentViewHolder

class RecentRecyclerAdapter: RecyclerView.Adapter<RecentViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<Content>()


    private lateinit var itemClickListener : OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        Log.d(TAG, "PostRecyclerAdapter - onCreateViewHolder() called")
                val binding = LayoutRecyclerRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RecentViewHolder(binding)
    }
    override fun getItemCount() = this.modelList.size

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        Log.d(TAG, "CommentRecyclerAdapter - onBindViewHolder() called / position: $position")
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
    fun submitList(modelList: ArrayList<Content>){
        Log.d(TAG, "submitList called")
        this.modelList.addAll(modelList)
        notifyDataSetChanged()
    }
    fun clearList(){
        modelList.clear()
    }
    fun getPostNo(position: Int):Int {
        return modelList[position].no
    }
}

