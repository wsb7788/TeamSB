package com.project.teamsb.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.project.teamsb.R
import com.project.teamsb.data.remote.main.MainListener
import com.project.teamsb.databinding.ActivityMainBinding

import com.project.teamsb.main.user.UserFragment
import com.project.teamsb.toolbar.NotificationActivity
import com.project.teamsb.toolbar.SearchActivity
import com.project.teamsb.toolbar.setting.AppGuideActivity
import com.project.teamsb.toolbar.setting.SettingActivity
import com.project.teamsb.ui.BaseActivity
import com.project.teamsb.ui.calendar.CalendarFragment
import com.project.teamsb.ui.home.HomeFragment
import com.project.teamsb.ui.notice.NoticeFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity:BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,MainListener {

    lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModel()


    lateinit var fragment: Fragment

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.mainListener = this

        botttomInit()

        viewModel.getUserNickname()

        val pref = getSharedPreferences("SettingInfo", MODE_PRIVATE)
        if(pref.getBoolean("isFirstLaunch",true)){

            val intent = Intent(this, AppGuideActivity::class.java)
            pref.edit().putBoolean("isFirstLaunch",false).apply()
            startActivity(intent)
        }
        binding.ivNotification.setOnClickListener(this)
        binding.ivSearch.setOnClickListener(this)
        binding.ivSetting.setOnClickListener(this)
    }
    override fun onResume() {
        super.onResume()
        viewModel.checkNotification()
    }
    private fun showTabCalendar(){
        val transaction = supportFragmentManager.beginTransaction()
        fragment = CalendarFragment()
        binding.tvToolbar.text = "식단표"
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun showTabHome(){
        val transaction = supportFragmentManager.beginTransaction()
        fragment = HomeFragment()
        binding.tvToolbar.text = "홈"
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
    private fun showTabNotice(){
        val transaction = supportFragmentManager.beginTransaction()
        fragment = NoticeFragment()
        binding.tvToolbar.text = "공지사항"
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun showTabUser(){
        val transaction = supportFragmentManager.beginTransaction()
        fragment = com.project.teamsb.ui.user.UserFragment()
        binding.tvToolbar.text = "내 글"
        transaction.replace(binding.fragment.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.navigation_calendar ->{
                showTabCalendar()
            }
            R.id.navigation_home ->{
                showTabHome()
            }
            R.id.navigation_notice ->{
                showTabNotice()
            }
            R.id.navigation_user ->{
                showTabUser()
            }
        }
        for(i in 0 until binding.bnv.menu.size()){
            binding.bnv.menu.getItem(i).isEnabled = binding.bnv.menu.getItem(i).itemId != item?.itemId
        }
        return true
    }
    override fun onBackPressed() {
        viewModel.backPressed()
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
    override fun backPressedMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun botttomInit() {
        val bottomNavigationView = findViewById<View>(binding.bnv.id) as BottomNavigationView             //OnNavigationItemSelectedListener 연결 aaa
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.navigation_home
        showTabHome()
    }

    override fun onNicknameFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun existNoti() {
        binding.ivNotification.setImageResource(R.drawable.ic_notification_new)
    }

    override fun noExistNoti() {
        binding.ivNotification.setImageResource(R.drawable.ic_notification)
    }

    override fun onNotiFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}