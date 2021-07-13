package com.project.teamsb

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityDeliveryBinding


class DeliveryActivity: AppCompatActivity() {
    val binding by lazy {ActivityDeliveryBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}