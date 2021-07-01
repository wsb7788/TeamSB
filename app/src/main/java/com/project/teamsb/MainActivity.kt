package com.project.teamsb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.project.teamsb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val id = binding.idEt.text.toString()
            val pw = binding.passwordEt.text.toString()
            if (id == "wsb7788" && pw == "1234") {

                val intent = Intent(
                    applicationContext,
                    FirstNicknameSetActivity::class.java
                )           // 서버 연결 후 로그인 여부 판별
                intent.putExtra("아이디",id)
                intent.putExtra("비밀번호",pw)
                startActivity(intent)
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("로그인 실패")
                    .setMessage("아이디 또는 비밀번호가 틀립니다.")
                    .setPositiveButton("확인", null)
                builder.show()
            }

        }
    }
}