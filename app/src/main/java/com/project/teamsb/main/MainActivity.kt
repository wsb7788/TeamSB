package com.project.teamsb.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.project.teamsb.R
import com.project.teamsb.api.ResultNickname
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.toolbar.SettingActivity
import com.project.teamsb.toolbar.WriteActivity
import com.project.teamsb.databinding.ActivityMainBinding
import com.project.teamsb.main.calendar.CalendarFragment
import com.project.teamsb.main.home.PostListActivity
import com.project.teamsb.main.home.HomeFragment
import com.project.teamsb.main.notice.NoticeFragment
import com.project.teamsb.toolbar.SearchActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity:AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val manager = supportFragmentManager
    var mBackWait:Long = 0

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var getNicknameService: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val bottomNavigationView = findViewById<View>(binding.bnv.id) as BottomNavigationView             //OnNavigationItemSelectedListener 연결 aaa
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.navigation_home
        ShowTabHome()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.setting)

        getUserInfo()
    }

    private fun getUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val prefInfo = getSharedPreferences("userInfo", MODE_PRIVATE)
            val editorInfo = prefInfo.edit()
            val id = prefInfo.getString("id","")!!

            try{
                getNicknameService.getUserNickname(id).enqueue(object :Callback<ResultNickname>{
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.write_tb -> {

                val intent = Intent(this, WriteActivity::class.java)
                intent.putExtra("category", "all")
                startActivity(intent)
            }
            R.id.search_tb -> {
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra("category", "all")
                startActivity(intent)
            }
            android.R.id.home -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)


    }

    fun ShowTabCalendar(){
        val transaction = manager.beginTransaction()
        val fragment = CalendarFragment()
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun ShowTabHome(){
        val transaction = manager.beginTransaction()
        val fragment = HomeFragment()
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun ShowTabNotice(){
        val transaction = manager.beginTransaction()
        val fragment = NoticeFragment()
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
        }
        return true
    }
    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Snackbar.make(binding.root,"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Snackbar.LENGTH_LONG).show()
        } else {
            moveTaskToBack(true);						// 태스크를 백그라운드로 이동
            finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}