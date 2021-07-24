package com.project.teamsb.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityLoginBinding
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
    var loginService: LoginAPI = retrofit.create(LoginAPI::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val pref = getSharedPreferences("loginCheck",MODE_PRIVATE)





        if(pref.getBoolean("autoLoginCheck",false)){
            val id = pref.getString("id","")!!
            val pw = pref.getString("pw","")!!

                loginService.requestLogin(id, pw).enqueue(object : Callback<ResultLogin> {
                    override fun onFailure(call: Call<ResultLogin>, t: Throwable) {
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<ResultLogin>,
                        response: Response<ResultLogin>
                    ) {
                        val intent = Intent(
                            applicationContext,
                            FirstNicknameSetActivity::class.java
                        )
                        intent.putExtra("아이디", id)
                        intent.putExtra("비밀번호", pw)
                        startActivity(intent)
                    }
                })

        }

        binding.loginBtn.setOnClickListener {
            val id = binding.idEt.text.toString()
            val pw = binding.passwordEt.text.toString()
            val pref = getSharedPreferences("loginCheck",MODE_PRIVATE)
            val editor = pref.edit()

            loginService.requestLogin(id,pw).enqueue(object: Callback<ResultLogin>{
                override fun onFailure(call: Call<ResultLogin>, t: Throwable) {
                    Toast.makeText(applicationContext,"로그인 실패",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ResultLogin>, response: Response<ResultLogin>) {
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
                }

        })
        }
    }

    /*private fun login(id: String?, pw: String?){

        val pref = getSharedPreferences("loginCheck",MODE_PRIVATE)
        val editor = pref.edit()

        loginService.requestLogin(id,pw).enqueue(object: Callback<ResultLogin>{
            override fun onFailure(call: Call<ResultLogin>, t: Throwable) {
                Toast.makeText(applicationContext,"로그인 실패",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResultLogin>, response: Response<ResultLogin>) {
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
            }

        })*/



        /*if (id == "wsb7788" && pw == "1234") {
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
        }*/

    }
