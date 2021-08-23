
package com.project.teamsb.recycler.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerGuideBinding
import com.project.teamsb.databinding.LayoutRecyclerNoticeBinding
import com.project.teamsb.recycler.hoder.GuideViewHolder
import com.project.teamsb.recycler.hoder.NoticeViewHolder
import com.project.teamsb.recycler.model.GuideModel
import com.project.teamsb.recycler.model.NoticeModel

class GuideRecyclerAdapter: RecyclerView.Adapter<GuideViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<GuideModel>()

    var expanded = ArrayList<Int>()
   // private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val binding = LayoutRecyclerGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuideViewHolder(binding)
    }
    override fun getItemCount() = this.modelList.size
    fun submitList(modelList: ArrayList<GuideModel>){
        this.modelList.addAll(modelList)
    }
    fun deleteLoading(){
        modelList.removeAt(modelList.lastIndex)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        for(i in 0 until expanded.size){
            if (position == expanded[i]){
                this.modelList[position].isVisible = true
            }
        }
        holder.bind(this.modelList[position])

        holder.itemView.setOnClickListener{
            //itemClickListener.onClick(it, position)

            if(!modelList[position].isVisible!!){
                holder.binding.clNoticeContent.visibility = View.VISIBLE
                holder.binding.tvSubContent.visibility = View.VISIBLE
                holder.binding.tvSubTitle.visibility = View.VISIBLE
                holder.binding.ivArrow.animate().setDuration(50).rotation(180f)
                holder.binding.clMother.setBackgroundColor(Color.parseColor("#e0e0e0"))
                modelList[position].isVisible = true
                expanded.add(position)
            }else{
                holder.binding.clNoticeContent.visibility = View.GONE
                holder.binding.tvSubContent.visibility = View.GONE
                holder.binding.tvSubTitle.visibility = View.GONE
                holder.binding.ivArrow.animate().setDuration(50).rotation(0f)
                holder.binding.clMother.setBackgroundColor(Color.parseColor("#ffffff"))
                modelList[position].isVisible = false
                expanded.remove(position)
            }
        }
    }


}
