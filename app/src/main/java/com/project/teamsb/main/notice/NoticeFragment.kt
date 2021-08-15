package com.project.teamsb.main.notice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.project.teamsb.R
import com.project.teamsb.databinding.FragmentHomeBinding
import com.project.teamsb.databinding.FragmentNoticeBinding

class NoticeFragment : Fragment(),View.OnClickListener {
    lateinit var binding: FragmentNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNoticeBinding.inflate(inflater, container, false)

        binding.cl.setOnClickListener(this)




        return inflater.inflate(R.layout.fragment_notice, container, false)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.cl ->{
                if (binding.tv.visibility == GONE){
                    binding.tv.visibility = VISIBLE
                    binding.cl.animate().duration = 1000
                    binding.cl.animate().rotation(180f)
                }else{
                    binding.tv.visibility = GONE
                    binding.cl.animate().duration = 1000
                }
            }
        }
    }
}