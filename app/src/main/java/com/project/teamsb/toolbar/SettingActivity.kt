package com.project.teamsb.toolbar

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.api.NicknameSet
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivitySettingBinding
import com.project.teamsb.databinding.DialogEditNicknameBinding
import com.project.teamsb.databinding.DialogReportBinding
import com.project.teamsb.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SettingActivity:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingBinding

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nicknameSet()


        binding.btnEditNickname.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnEditNickname ->{
                nicknameDialog()
            }
        }

    }

    private fun nicknameDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = DialogEditNicknameBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.btnPositive.setOnClickListener {
            var text = view.etNickname.text.toString()
            text.replace(" ", "")
            if(text.isNullOrBlank()) {
                Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
            }else if(text.length < 2 || text.length > 10){
                Toast.makeText(this, "2~10자의 닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            }else {
                nicknameMod(text)
                alertDialog.onBackPressed()
            }

        }
        view.btnNegative.setOnClickListener {
            alertDialog.onBackPressed()
        }
    }
    private fun nicknameMod(nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val editUser = pref.edit()
                val id = pref.getString("id","")!!
                serverAPI.nicknameSet(id, nickname).enqueue(object : Callback<NicknameSet> {
                    override fun onResponse(call: Call<NicknameSet>, response: Response<NicknameSet>) {
                        if (response.body()!!.check) {
                            Toast.makeText(applicationContext, "닉네임 변경 완료", Toast.LENGTH_SHORT).show()
                            editUser.putString("nickname",nickname)
                            editUser.apply()
                            nicknameSet()
                        } else {
                            Toast.makeText(applicationContext, "${response.body()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<NicknameSet>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun nicknameSet(){
        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        binding.tvNickname.text = pref.getString("nickname","")
    }
}