package com.project.teamsb.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.project.teamsb.R
import com.project.teamsb.data.remote.login.LoginListener
import com.project.teamsb.databinding.ActivityLoginBinding
import com.project.teamsb.login.FirstNicknameSetActivity
import com.project.teamsb.ui.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity:BaseActivity(),LoginListener {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

    }





    override fun onLoginStarted() {
        Log.d("로그","1")
        binding.progressBar.visibility = VISIBLE
        Log.d("로그","1")
    }

    override fun onLoginSuccess() {
        Log.d("로그","2")
        binding.progressBar.visibility = INVISIBLE
        Log.d("로그","2")
        val intent = Intent(this, FirstNicknameSetActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginFailure(message: String) {
        Log.d("로그","3")
        binding.progressBar.visibility = INVISIBLE
        Log.d("로그","3")
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()


    }
}