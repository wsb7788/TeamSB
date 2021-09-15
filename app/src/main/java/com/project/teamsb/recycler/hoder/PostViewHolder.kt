package com.project.teamsb.recycler.hoder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.ApplicationClass
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerPostListBinding
import com.project.teamsb.post.App
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.recycler.model.PostModel


class PostViewHolder(val binding: LayoutRecyclerPostListBinding): RecyclerView.ViewHolder(binding.root){


    val TAG: String = "로그"





    fun bind(postModel: PostModel){
        binding.ivNickname.text = postModel.nickname
        binding.tvCategory.text = postModel.category
        binding.tvTitle.text = postModel.title
        when(postModel.keyword!!.size){
            1->{
                binding.keyword1.text = "# "
                binding.tvKeyword1.text = postModel.keyword!![0]
            }
            2->{
                binding.keyword1.text = "# "
                binding.keyword2.text = "# "
                binding.tvKeyword1.text = postModel.keyword!![0]
                binding.tvKeyword2.text = postModel.keyword!![1]
            }
            3->{
                binding.keyword1.text = "# "
                binding.keyword2.text = "# "
                binding.keyword3.text = "# "
                binding.tvKeyword1.text = postModel.keyword!![0]
                binding.tvKeyword2.text = postModel.keyword!![1]
                binding.tvKeyword3.text = postModel.keyword!![2]
            }
        }


        binding.tvComment.text = postModel.comment
        binding.tvTimeStamp.text = postModel.time

        Glide
            .with(ApplicationClass.instance)
            .load(postModel.profileImage)
            .circleCrop()
            .placeholder(R.drawable.profile_basic)
            .into(binding.ivProfile)

    }



}