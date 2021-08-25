package com.project.teamsb.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.R
import com.project.teamsb.databinding.GuideListItemBinding

class AppGuideAdapter(imgList:ArrayList<Int>):RecyclerView.Adapter<AppGuideAdapter.PagerViewHolder>() {
    var guide = imgList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = GuideListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PagerViewHolder(binding)
    }


    override fun getItemCount() = guide.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.img.setImageResource(guide[position])
    }



    inner class PagerViewHolder(val binding :GuideListItemBinding): RecyclerView.ViewHolder(binding.root){

        val img = binding.ivGuide
    }
}