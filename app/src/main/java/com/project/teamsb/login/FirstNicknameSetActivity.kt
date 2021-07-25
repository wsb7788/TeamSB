package com.project.teamsb.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.main.MainActivity
import com.project.teamsb.databinding.ActivityFirstnicknamesetBinding
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FirstNicknameSetActivity : AppCompatActivity(),View.OnClickListener {


    private val binding by lazy { ActivityFirstnicknamesetBinding.inflate(layoutInflater) }

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var loginService: LoginAPI = retrofit.create(LoginAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.checkBtn.setOnClickListener(this)
        binding.setBtn.setOnClickListener(this)



    }

    override fun onClick(v: View?) {
        val nickName = binding.nicknameEt.text.toString()
        val id = intent.getStringExtra("아이디")!!

        when(v){
            binding.checkBtn -> {
                loginService.nicknameCheck(nickName).enqueue(object : Callback<NicknameCheck> {
                    override fun onFailure(call: Call<NicknameCheck>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<NicknameCheck>,
                        response: Response<NicknameCheck>
                    ) {
                        val body = response.body()!!
                        if (body.check) {
                            Toast.makeText(applicationContext, "사용 가능", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "${body.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }

            binding.setBtn ->{

                loginService.nicknameSet(id,nickName).enqueue(object :Callback<NicknameSet>{
                    override fun onResponse(call: Call<NicknameSet>, response: Response<NicknameSet>) {
                        if(response.body()!!.code == 200){
                            Toast.makeText(applicationContext, "${response.body()}", Toast.LENGTH_SHORT).show()
                            Log.d("로그","${response.body()}")
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Log.d("로그","else called")
                            Toast.makeText(applicationContext, "${response.body()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<NicknameSet>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })}
            }
        }

    }


