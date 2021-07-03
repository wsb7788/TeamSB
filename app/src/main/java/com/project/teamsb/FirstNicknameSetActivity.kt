package com.project.teamsb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityFirstnicknamesetBinding

class FirstNicknameSetActivity : AppCompatActivity() {


    private val binding by lazy { ActivityFirstnicknamesetBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.checkBtn.setOnClickListener {
            // 서버랑 연동해서 닉네임 중복여부 판별
        }
        binding.setBtn.setOnClickListener {
            // 서버랑 연동해서 닉네임 설정
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }




    }
}