package com.project.teamsb.ui.nickname


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.project.teamsb.R
import com.project.teamsb.data.remote.nickname.NicknameListener
import com.project.teamsb.databinding.ActivityFirstnicknamesetBinding
import com.project.teamsb.main.MainActivity
import com.project.teamsb.ui.BaseActivity
import com.project.teamsb.utils.hideKeyboard
import org.koin.android.viewmodel.ext.android.viewModel

class NicknameActivity : BaseActivity(), NicknameListener {

    lateinit var binding: ActivityFirstnicknamesetBinding
    private val viewModel: NicknameViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_firstnicknameset)
        binding.lifecycleOwner = this
        binding.nicknameViewModel = viewModel
        viewModel.nicknameListener = this

        binding.checkBtn.setOnClickListener(this)
        binding.setBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        binding.root.hideKeyboard()
        when (v) {
            binding.checkBtn -> {
                viewModel.nicknameCheck()
            }
            binding.setBtn -> {
                viewModel.nicknameSet()
            }
        }
    }

    override fun onNicknameSetFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun onNicknameCheckResponse(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun onNicknameSetSuccess() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}


