package com.project.teamsb.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.project.teamsb.CalendarFragment
import com.project.teamsb.R
import com.project.teamsb.api.*
import com.project.teamsb.toolbar.SettingActivity
import com.project.teamsb.databinding.ActivityMainBinding
import com.project.teamsb.login.LoginActivity
import com.project.teamsb.main.home.HomeFragment
import com.project.teamsb.main.notice.NoticeFragment
import com.project.teamsb.main.user.UserFragment
import com.project.teamsb.toolbar.NotificationActivity
import com.project.teamsb.toolbar.SearchActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity:AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val manager = supportFragmentManager
    var mBackWait:Long = 0
    var isNotiCheck = false
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        startService(Intent(this, ForcedTerminationService::class.java))


        val bottomNavigationView = findViewById<View>(binding.bnv.id) as BottomNavigationView             //OnNavigationItemSelectedListener 연결 aaa
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.navigation_home

        ShowTabHome()
        getUserInfo()


        binding.ivNotification.setOnClickListener(this)
        binding.ivSearch.setOnClickListener(this)
        binding.ivSetting.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        checkNotification()
    }

    private fun getUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val prefInfo = getSharedPreferences("userInfo", MODE_PRIVATE)
            val editorInfo = prefInfo.edit()
            val id = prefInfo.getString("id","")!!

            try{
                serverAPI.getUserNickname(id).enqueue(object :Callback<ResultNickname>{
                    override fun onResponse(call: Call<ResultNickname>, response: Response<ResultNickname>) {
                        if (response.body()!!.check){
                            editorInfo.putString("nickname",response.body()!!.content)
                            editorInfo.apply()
                        }else{
                            Toast.makeText(applicationContext, response.body()!!.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultNickname>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 실패 !",Toast.LENGTH_SHORT).show()
                    }
                })
            }catch (e:Exception){
                Toast.makeText(applicationContext, "인터넷 오류",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkNotification() {
        CoroutineScope(Dispatchers.IO).launch {
            val pref= getSharedPreferences("userInfo", MODE_PRIVATE)
            val id = pref.getString("id","")!!
            try {
                serverAPI.notiCheck("notification/check",id).enqueue(object :
                    Callback<ResultNotiCheck>{
                    override fun onResponse(call: Call<ResultNotiCheck>, response: Response<ResultNotiCheck>) {
                        if(response.body()!!.check){
                            if(response.body()!!.notificationCount == 0){
                                isNotiCheck = false
                                binding.ivNotification.setImageResource(R.drawable.ic_notification)
                            }else{
                                isNotiCheck = true
                                binding.ivNotification.setImageResource(R.drawable.ic_notification_new)
                            }
                        }else{
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultNotiCheck>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun ShowTabCalendar(){
        val transaction = manager.beginTransaction()
        val fragment = CalendarFragment()
        binding.tvToolbar.text = "식단표"
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun ShowTabHome(){
        val transaction = manager.beginTransaction()
        val fragment = HomeFragment()
        binding.tvToolbar.text = "홈"
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun ShowTabNotice(){
        val transaction = manager.beginTransaction()
        val fragment = NoticeFragment()
        binding.tvToolbar.text = "공지사항"
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun ShowTabUser(){
        val transaction = manager.beginTransaction()
        val fragment = UserFragment()
        binding.tvToolbar.text = "내 글"
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.navigation_calendar ->{
                ShowTabCalendar()
            }
            R.id.navigation_home ->{
                ShowTabHome()
            }
            R.id.navigation_notice ->{
                ShowTabNotice()
            }
            R.id.navigation_user ->{
                ShowTabUser()
            }
        }
        for(i in 0 until binding.bnv.menu.size()){
            binding.bnv.menu.getItem(i).isEnabled = binding.bnv.menu.getItem(i).itemId != item?.itemId
        }
        return true
    }
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Snackbar.make(binding.root,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Snackbar.LENGTH_SHORT).show()
        } else {
            deleteTokenAndFinish()
        }
    }
    private fun deleteTokenAndFinish() {
        CoroutineScope(Dispatchers.IO).launch {
            val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
            val edit = pref.edit()
            val id = pref.getString("id","")!!
            if(!pref.getBoolean("autoLoginSuccess",false)){
                try {
                    serverAPI.getToken(id,null).enqueue(object : Callback<ResultNoReturn> {
                        override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                            Toast.makeText(applicationContext, "서버통신 오류", Toast.LENGTH_SHORT).show()
                            moveTaskToBack(true);						// 태스크를 백그라운드로 이동
                            finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                        override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                            if (response.body()!!.check) {
                                edit.clear()
                                edit.commit()
                                Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                            }
                            moveTaskToBack(true);						// 태스크를 백그라운드로 이동
                            finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivNotification -> {
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
            }
            binding.ivSearch -> {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("category", "all")
                startActivity(intent)
            }
            binding.ivSetting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}