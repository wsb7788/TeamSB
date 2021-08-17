
package com.project.teamsb.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutRecyclerNoticeBinding
import com.project.teamsb.recycler.hoder.NoticeViewHolder
import com.project.teamsb.recycler.model.NoticeModel

class NoticeRecyclerAdapter: RecyclerView.Adapter<NoticeViewHolder>() {

    val TAG: String = "로그"

    private var modelList = ArrayList<NoticeModel>()

   // private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = LayoutRecyclerNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }
    override fun getItemCount() = this.modelList.size
    fun submitList(modelList: ArrayList<NoticeModel>){
        this.modelList.addAll(modelList)
    }
    fun deleteLoading(){
        modelList.removeAt(modelList.lastIndex)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(this.modelList[position])
       /* holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }*/
    }


/*
interface OnItemClickListener {
    fun onClick(v: View, position: Int)
}
fun setItemClickListener(onItemClickListener: OnItemClickListener) {
    this.itemClickListener = onItemClickListener
}
    fun clearList(){
        modelList.clear()
    }
*/

    fun getNoticeNo(position:Int) = modelList[position].notice_no

}
