package com.project.teamsb.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)

    lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val pref = getSharedPreferences("autoLogin", MODE_PRIVATE)
        val editor = pref.edit()

        if (pref.getBoolean("autoLoginCheck", false)) {
            val id = pref.getString("id", "")!!
            val pw = pref.getString("pw", "")!!

            login(id, pw)

        }
        binding.loginBtn.setOnClickListener(this)

        binding.autoLoginCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editor.putBoolean("autoLoginCheck", true)
                editor.apply()
            }else{
                editor.clear()
                editor.apply()
            }
        }

    }



    private fun login(id: String, pw: String) {
        binding.progressBar.visibility = VISIBLE
        CoroutineScope(Dispatchers.IO).launch {

            val id= id
            val pw = pw

            val prefAuto = getSharedPreferences("autoLogin", MODE_PRIVATE)
            val editorAuto = prefAuto.edit()

            val prefInfo = getSharedPreferences("userInfo", MODE_PRIVATE)
            val editorInfo = prefInfo.edit()

            try{
                serverAPI.requestLogin(id, pw).enqueue(object : Callback<ResultLogin> {
                    override fun onFailure(call: Call<ResultLogin>, t: Throwable) {
                        //메인스레드
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = INVISIBLE
                    }

                    override fun onResponse(call: Call<ResultLogin>, response: Response<ResultLogin>) {
                        binding.progressBar.visibility = INVISIBLE
                        if(response.body()!!.check){
                            editorInfo.putString("id", id)
                            editorInfo.apply()
                            if(binding.autoLoginCb.isChecked){
                                editorAuto.putString("id",id)
                                editorAuto.putString("pw",pw)
                                editorAuto.apply()
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
                binding.progressBar.visibility = INVISIBLE
                e.printStackTrace()
            }

        }
    }

    override fun onClick(v: View?) {

        when(v){
            binding.loginBtn -> {
                imm.hideSoftInputFromWindow(v.windowToken,0)
                val id = binding.idEt.text.toString()
                val pw = binding.passwordEt.text.toString()
                login(id, pw)
            }
        }
    }
}

