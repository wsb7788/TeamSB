package com.project.teamsb

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivitySearchBinding
import com.project.teamsb.databinding.ActivitySettingBinding

class SearchActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}