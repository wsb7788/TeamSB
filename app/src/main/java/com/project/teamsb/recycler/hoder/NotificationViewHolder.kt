package com.project.teamsb.recycler.hoder

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.databinding.LayoutRecyclerCommentBinding
import com.project.teamsb.databinding.LayoutRecyclerNotificationBinding
import com.project.teamsb.post.App
import com.project.teamsb.recycler.model.CommentModel
import com.project.teamsb.recycler.model.NotificationModel

class NotificationViewHolder(val binding: LayoutRecyclerNotificationBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(notificationModel: NotificationModel){
        if(!notificationModel.checkRead!!){
            binding.mother.setBackgroundColor(Color.parseColor("#FFF9E2"))
        }else{
            binding.mother.setBackgroundColor(Color.parseColor("#ffffff"))
        }





        val text = notificationModel.nickname + notificationModel.title+" \""+ notificationModel.content+"\" "+notificationModel.timeStamp
        val spannableString = SpannableString(text)
        spannableString.setSpan(StyleSpan(Typeface.BOLD),0,notificationModel.nickname!!.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#979797")),text.length-notificationModel.timeStamp!!.length,text.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(RelativeSizeSpan(0.8f),text.length-notificationModel.timeStamp!!.length,text.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvNotificationContent.text = spannableString
        Glide
            .with(App.instance)
            .load(notificationModel.imageSoruce)
            .circleCrop()
            .placeholder(R.drawable.profile_basic)
            .into(binding.ivNotificationProfile)


    }

}