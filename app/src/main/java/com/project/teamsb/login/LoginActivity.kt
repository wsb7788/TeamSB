package com.project.teamsb.login

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.R
import com.project.teamsb.api.ResultLogin
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivityLoginBinding
import com.project.teamsb.main.MainActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var loginService: ServerAPI = retrofit.create(ServerAPI::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val pref = getSharedPreferences("loginCheck", MODE_PRIVATE)
        val editor = pref.edit()

        if (pref.getBoolean("autoLoginCheck", false)) {
            val id = pref.getString("id", "")!!
            val pw = pref.getString("pw", "")!!

            login(id, pw)

        }

        binding.autoLoginCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor.putBoolean("autoLoginCheck", true)
                editor.apply()
            }else{
                editor.putBoolean("autoLoginCheck", false)
                editor.apply()
            }
        }
        binding.loginBtn.setOnClickListener {
            val id = binding.idEt.text.toString()
            val pw = binding.passwordEt.text.toString()
            login(id, pw)
        }
    }



    private fun login(id: String, pw: String) {
        CoroutineScope(Dispatchers.IO).launch {

            val id= id
            val pw = pw

            val prefAuto = getSharedPreferences("autoLoginCheck", MODE_PRIVATE)
            val editorAuto = prefAuto.edit()

            val prefId = getSharedPreferences("userId", MODE_PRIVATE)
            val editorId = prefId.edit()

            try{
                loginService.requestLogin(id, pw).enqueue(object : Callback<ResultLogin> {
                    override fun onFailure(call: Call<ResultLogin>, t: Throwable) {
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResultLogin>, response: Response<ResultLogin>) {
                        if(response.body()!!.check){
                            editorId.putString("id", id)
                            editorId.apply()
                            if(prefAuto.getBoolean("autoLoginCheck",false)){
                                editorAuto.putString("id",id)
                                editorAuto.putString("pw",pw)
                            }
                            if(response.body()!!.nickname){
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                            }else{
                                val intent = Intent(applicationContext, FirstNicknameSetActivity::class.java)
                                startActivity(intent)
                            }
                        }else{
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

