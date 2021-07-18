package com.project.teamsb.main.home

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.project.teamsb.databinding.ActivityDeliveryBinding
import com.project.teamsb.databinding.ActivityFoodBinding


class FoodActivity: AppCompatActivity() {
    val binding by lazy {ActivityFoodBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}