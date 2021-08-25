package com.project.teamsb.toolbar.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.project.teamsb.R
import com.project.teamsb.databinding.ActivityAppGuideBinding
import com.project.teamsb.recycler.adapter.AppGuideAdapter

class AppGuideActivity:AppCompatActivity() {

    private lateinit var binding:ActivityAppGuideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vp.adapter = AppGuideAdapter(getImgList())
        binding.vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

    }

    private fun getImgList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.guide_1,R.drawable.guide_2, R.drawable.guide_3)
    }
}