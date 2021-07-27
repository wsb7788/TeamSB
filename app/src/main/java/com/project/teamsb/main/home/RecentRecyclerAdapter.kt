
package com.project.teamsb.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.databinding.LayoutRecyclerProgressBinding
import com.project.teamsb.databinding.LayoutRecyclerRecentBinding
import com.project.teamsb.post.ProgressViewHolder

class RecentRecyclerAdapter: RecyclerView.Adapter<RecentViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<RecentModel>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        Log.d(TAG, "PostRecyclerAdapter - onCreateViewHolder() called")
                val binding = LayoutRecyclerRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return RecentViewHolder(binding)
    }
    override fun getItemCount() = this.modelList.size
    fun deleteLoading(){
        Log.d(TAG, "deleteLoading called")
        modelList.removeAt(modelList.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        Log.d(TAG, "CommentRecyclerAdapter - onBindViewHolder() called / position: $position")
            holder.bind(this.modelList[position])
    }
    fun submitList(modelList: ArrayList<RecentModel>){
        Log.d(TAG, "submitList called")
        this.modelList.addAll(modelList)
    }
}

