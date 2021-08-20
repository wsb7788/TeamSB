package com.project.teamsb.login

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.R
import com.project.teamsb.databinding.ActivitySplashBinding
import com.project.teamsb.main.MainActivity

class SplashActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onResume() {
        super.onResume()
        var intent = Intent()
        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        if (pref.getBoolean("autoLoginSuccess", false)) {
            if(pref.getString("nickname","").isNullOrBlank()){
                intent = Intent(applicationContext, FirstNicknameSetActivity::class.java)

            }else{
                intent = Intent(applicationContext, MainActivity::class.java)
            }
        }else{
            intent = Intent(this, LoginActivity::class.java)
        }

        val anim_in = AnimationUtils.loadAnimation(this,R.anim.splash_out)
        anim_in.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                binding.ivGif.visibility = INVISIBLE
                val intent= intent
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in,R.anim.splash_in)
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        binding.ivGif.startAnimation(anim_in)
    }

    override fun onBackPressed() {

    }
}