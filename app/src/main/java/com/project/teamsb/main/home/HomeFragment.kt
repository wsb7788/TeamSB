package com.project.teamsb.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.teamsb.databinding.FragmentHomeBinding


class HomeFragment : Fragment(),View.OnClickListener {

    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        return  binding.root
    }
    override fun onClick(v: View){

        when(v){

            binding.btn1 ->{
                val intent = Intent(
                    activity,
                    FoodActivity::class.java
                )
                startActivity(intent)
            }
            binding.btn2 ->{
                val intent = Intent(
                    activity,
                    DeliveryActivity::class.java
                )
                startActivity(intent)
            }
            binding.btn3 ->{
                val intent = Intent(
                    activity,
                    TaxiActivity::class.java
                )
                startActivity(intent)
            }
            binding.btn4 ->{
                val intent = Intent(
                    activity,
                    LaundryActivity::class.java
                )
                startActivity(intent)
            }
        }
    }

}