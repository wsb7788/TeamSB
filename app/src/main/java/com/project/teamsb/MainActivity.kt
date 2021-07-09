package com.project.teamsb

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.teamsb.databinding.ActivityMainBinding

class MainActivity:AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val manager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val bottomNavigationView = findViewById<View>(binding.bnv.id) as BottomNavigationView             //OnNavigationItemSelectedListener 연결 a
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        bottomNavigationView.selectedItemId = R.id.navigation_home
        ShowTabHome()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
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
}