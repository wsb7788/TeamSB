package com.project.teamsb.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.databinding.LayoutTopBannerBinding
import com.project.teamsb.recycler.hoder.TopBannerViewHolder
import com.project.teamsb.recycler.model.TopBannerModel

class TopBannerAdapter: RecyclerView.Adapter<TopBannerViewHolder>() {

    var modelList=ArrayList<TopBannerModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopBannerViewHolder {
        val binding = LayoutTopBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopBannerViewHolder, position: Int) {
        holder.bind(this.modelList[position])            //position에 넣은 배열 크기만큼 나눈 나머지 값을 사용한다
    }
    fun submitList(modelList: ArrayList<TopBannerModel>){
        this.modelList.addAll(modelList)

    }
    override fun getItemCount() = modelList.size    //무한 로딩을 위해 임의로 갯수를 늘린다.
    fun clearList(){
        modelList.clear()
    }
}