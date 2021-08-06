package com.project.teamsb.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
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

    var mBackWait:Long = 0

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

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("로그", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("로그", token!!)
            Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
        })

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val pref = getSharedPreferences("autoLogin", MODE_PRIVATE)
        val editorAuto = pref.edit()
        if (pref.getBoolean("autoLoginCheck", false)) {
            binding.autoLoginCb.isChecked = true
            val id = pref.getString("id", "")!!
            val pw = pref.getString("pw", "")!!
            if(id.isNotBlank() || id.isNotEmpty()){
                login(id, pw)
            }


        }
        binding.loginBtn.setOnClickListener(this)

        binding.autoLoginCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editorAuto.putBoolean("autoLoginCheck", true)
                editorAuto.apply()
            }else{
                editorAuto.clear()
                editorAuto.apply()
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Snackbar.make(binding.root,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Snackbar.LENGTH_LONG).show()
        } else {
            val pref = getSharedPreferences("autoLogin", MODE_PRIVATE)
            val editorAuto = pref.edit()
            editorAuto.clear()
            editorAuto.apply()
            moveTaskToBack(true);						// 태스크를 백그라운드로 이동
            finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}

