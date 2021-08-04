package com.project.teamsb.toolbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityAlertBinding

class AlertActivity:AppCompatActivity() {

    val binding by lazy { ActivityAlertBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}