package com.project.teamsb.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.project.teamsb.R
import com.project.teamsb.data.remote.splash.SplashListener
import com.project.teamsb.databinding.ActivitySplashBinding
import com.project.teamsb.login.FirstNicknameSetActivity
import com.project.teamsb.main.MainActivity
import com.project.teamsb.ui.BaseActivity
import com.project.teamsb.ui.login.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity:BaseActivity(),SplashListener {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel:SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        binding.lifecycleOwner = this
        binding.splashViewModel = viewModel
        viewModel.splashListener = this

        showSplash()


    }

    private fun showSplash() {
        val animIn = AnimationUtils.loadAnimation(this,R.anim.splash_out)
        animIn.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                binding.ivGif.visibility = INVISIBLE
                viewModel.checkAutoLogin()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        binding.ivGif.startAnimation(animIn)
    }
    override fun onBackPressed() {

    }

    override fun onStartLogin() {
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStartMain() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}