package com.project.teamsb.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.project.teamsb.R
import com.project.teamsb.data.remote.login.LoginListener
import com.project.teamsb.databinding.ActivityLoginBinding
import com.project.teamsb.login.FirstNicknameSetActivity
import com.project.teamsb.ui.BaseActivity
import com.project.teamsb.ui.nickname.NicknameActivity
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity:BaseActivity(),LoginListener {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.loginListener = this
    }

    override fun onBackPressed() {
        viewModel.backPressed()
    }

    override fun onLoginStarted() {
        binding.progressBar.visibility = VISIBLE
    }

    override fun onLoginSuccess() {
        binding.progressBar.visibility = INVISIBLE
        val intent = Intent(this, NicknameActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginFailure(message: String) {
        binding.progressBar.visibility = INVISIBLE
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun backPressedMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}