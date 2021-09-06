package com.project.teamsb.ui.notice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.api.NoticeContent
import com.project.teamsb.api.ResponseNotice
import com.project.teamsb.databinding.FragmentNoticeBinding
import com.project.teamsb.api.ServerObj
import com.project.teamsb.data.remote.notice.NoticeListener
import com.project.teamsb.recycler.adapter.NoticeRecyclerAdapter
import com.project.teamsb.recycler.model.NoticeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NoticeFragment : Fragment(),NoticeListener{
    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!
    private val viewModel:NoticeViewModel by viewModel()

    lateinit var noticeRecyclerAdapter: NoticeRecyclerAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.noticeListener = this

        recyclerInit()


        binding.rcv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition == itemTotalCount) {
                    if (!viewModel.loadLock.value!!&&!viewModel.noMoreItem.value!!) {
                        viewModel.loadLock.value = true
                        viewModel.pageplus()
                        viewModel.noticeLoading()
                    }
                }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        noticeRecyclerAdapter.clearList()
        viewModel.pageInit()
        viewModel.topNoticeLoading()
    }


    override fun recyclerInit() {
        noticeRecyclerAdapter = NoticeRecyclerAdapter()
        binding.rcv.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = noticeRecyclerAdapter
        }
        viewModel.liveData.observe(viewLifecycleOwner,{
            if(viewModel.isTopLoading.value!!){
                noticeRecyclerAdapter.submitTopList(viewModel.topCount.value!!,it)
            }else{
                noticeRecyclerAdapter.submitList(viewModel.page.value!!,it)
            }
        })
    }

    override fun apiFailure(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }


}