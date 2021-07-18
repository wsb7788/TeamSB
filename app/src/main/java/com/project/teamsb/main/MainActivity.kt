package com.project.teamsb.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.teamsb.R
import com.project.teamsb.toolbar.SearchActivity
import com.project.teamsb.toolbar.SettingActivity
import com.project.teamsb.toolbar.WriteActivity
import com.project.teamsb.databinding.ActivityMainBinding
import com.project.teamsb.main.home.HomeFragment

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity:AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val manager = supportFragmentManager
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



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.write_tb -> {
                val intent = Intent(this, WriteActivity::class.java)
                startActivity(intent)
            }
            R.id.search_tb -> {
                val intent = Intent(this, SearchActivity::class.java)
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
}