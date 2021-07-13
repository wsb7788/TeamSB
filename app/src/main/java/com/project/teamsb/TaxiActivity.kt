package com.project.teamsb

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityDeliveryBinding
import com.project.teamsb.databinding.ActivityTaxiBinding


class TaxiActivity: AppCompatActivity() {
    val binding by lazy {ActivityTaxiBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}