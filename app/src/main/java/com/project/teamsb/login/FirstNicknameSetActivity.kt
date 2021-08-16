package com.project.teamsb.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.api.NicknameCheck
import com.project.teamsb.api.NicknameSet
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.main.MainActivity
import com.project.teamsb.databinding.ActivityFirstnicknamesetBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FirstNicknameSetActivity : AppCompatActivity(),View.OnClickListener {


    private val binding by lazy { ActivityFirstnicknamesetBinding.inflate(layoutInflater) }

    lateinit var id:String
    lateinit var imm:InputMethodManager
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val prefId = getSharedPreferences("userInfo", MODE_PRIVATE)
        id = prefId.getString("id", "")!!

        binding.checkBtn.setOnClickListener(this)
        binding.setBtn.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        imm.hideSoftInputFromWindow(v!!.windowToken,0)
        val nickName = binding.nicknameEt.text.toString()
        when (v) {
            binding.checkBtn -> {
                nicknameCheck(nickName)
            }
            binding.setBtn -> {
                nicknameSet(nickName, id)
            }
        }
    }

    private fun nicknameCheck(nickName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                serverAPI.nicknameCheck(nickName).enqueue(object : Callback<NicknameCheck> {
                    override fun onFailure(call: Call<NicknameCheck>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<NicknameCheck>, response: Response<NicknameCheck>) {
                        val body = response.body()!!
                        if (body.check) {
                            Toast.makeText(applicationContext, "사용 가능", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "${body.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun nicknameSet(nickName: String, id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                serverAPI.nicknameSet(id, nickName).enqueue(object : Callback<NicknameSet> {
                    override fun onResponse(call: Call<NicknameSet>, response: Response<NicknameSet>) {
                        if (response.body()!!.check) {
                            val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                            val edit = pref.edit()
                            edit.putString("nickname", nickName)
                            edit.apply()
                            Toast.makeText(applicationContext, "${response.body()}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
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
}


