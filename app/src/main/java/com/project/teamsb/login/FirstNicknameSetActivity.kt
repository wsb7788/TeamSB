package com.project.teamsb.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.api.NicknameCheck
import com.project.teamsb.api.NicknameSet
import com.project.teamsb.main.MainActivity
import com.project.teamsb.databinding.ActivityFirstnicknamesetBinding
import com.project.teamsb.api.ServerObj
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstNicknameSetActivity : AppCompatActivity(),View.OnClickListener {


    private val binding by lazy { ActivityFirstnicknamesetBinding.inflate(layoutInflater) }

    lateinit var id:String
    lateinit var imm:InputMethodManager

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
                ServerObj.api.nicknameCheck(nickName).enqueue(object : Callback<NicknameCheck> {
                    override fun onFailure(call: Call<NicknameCheck>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<NicknameCheck>, response: Response<NicknameCheck>) {
                        val body = response.body()!!
                        if (body.code==200) {
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

                ServerObj.api.nicknameSet(id, nickName).enqueue(object : Callback<NicknameSet> {
                    override fun onResponse(call: Call<NicknameSet>, response: Response<NicknameSet>) {
                        if (response.body()!!.code == 200) {
                            val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                            val edit = pref.edit()
                            edit.putString("nickname", nickName)
                            edit.apply()
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
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


