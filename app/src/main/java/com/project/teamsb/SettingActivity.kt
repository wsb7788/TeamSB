package com.project.teamsb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivitySearchBinding
import com.project.teamsb.databinding.ActivitySettingBinding

class SettingActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}