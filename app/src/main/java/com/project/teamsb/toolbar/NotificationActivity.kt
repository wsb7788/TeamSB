package com.project.teamsb.toolbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityNotificationBinding

class NotificationActivity:AppCompatActivity() {

    val binding by lazy { ActivityNotificationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}