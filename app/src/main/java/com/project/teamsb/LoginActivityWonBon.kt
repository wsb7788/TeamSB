package com.project.teamsb

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityLoginBinding



class LoginActivityWonBon : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val pref = getSharedPreferences("loginCheck",MODE_PRIVATE)





        if(pref.getBoolean("autoLoginCheck",false)){

            login(pref.getString("id",""),pref.getString("pw",""))
        }

        binding.loginBtn.setOnClickListener {
            val id = binding.idEt.text.toString()
            val pw = binding.passwordEt.text.toString()
            login(id,pw)
        }
    }

    private fun login(id: String?, pw: String?){

        val pref = getSharedPreferences("loginCheck",MODE_PRIVATE)
        val editor = pref.edit()

        if (id == "wsb7788" && pw == "1234") {
            val intent = Intent(
                applicationContext,
                FirstNicknameSetActivity::class.java
            )                                               // 서버 연결 후 로그인 여부 판별
            intent.putExtra("아이디",id)
            intent.putExtra("비밀번호",pw)

            if(binding.autoLoginCb.isChecked){
                editor.putBoolean("autoLoginCheck",true)
                editor.putString("id",id)
                editor.putString("pw",pw)
            } else{
                editor.clear()
            }

            editor.apply()
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