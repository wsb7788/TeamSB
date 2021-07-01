package com.project.teamsb

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityFirstnicknamesetBinding

class FirstNicknameSetActivity : AppCompatActivity() {


    val binding by lazy { ActivityFirstnicknamesetBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}