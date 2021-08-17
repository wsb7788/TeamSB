package com.project.teamsb.main.notice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.R
import com.project.teamsb.api.NoticeContent
import com.project.teamsb.api.ResponseNotice
import com.project.teamsb.databinding.FragmentHomeBinding
import com.project.teamsb.databinding.FragmentNoticeBinding
import com.project.teamsb.main.calendar.CalendarObj
import com.project.teamsb.recycler.adapter.NoticeRecyclerAdapter
import com.project.teamsb.recycler.model.NoticeModel
import com.project.teamsb.recycler.model.RecentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.security.auth.callback.Callback

class NoticeFragment : Fragment(){
    lateinit var binding: FragmentNoticeBinding

    lateinit var noticeRecyclerAdapter: NoticeRecyclerAdapter

    var modelList = ArrayList<NoticeModel>()
    var page = 1
    var loadLock = false
    var noMoreItem = false
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNoticeBinding.inflate(inflater, container, false)

        noticeRecyclerAdapter = NoticeRecyclerAdapter()

        binding.rcv.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = noticeRecyclerAdapter
        }



        binding.rcv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition == itemTotalCount) {
                    if (!loadLock&&!noMoreItem) {
                        loadLock = true
                        page++
                        noticeLoading()
                    }
                }
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dataLoading()
        noticeLoading()
    }

    private fun dataLoading() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                CalendarObj.api.noticeTopList(page).enqueue(object:retrofit2.Callback<ResponseNotice>{
                    override fun onResponse(call: Call<ResponseNotice>, response: Response<ResponseNotice>) {
                        if (response.body()!!.code==200){
                            if(!response.body()!!.content.isNullOrEmpty()){
                                setContent(response.body()!!.content)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseNotice>, t: Throwable) {
                        Toast.makeText(activity, "통신 에러", Toast.LENGTH_SHORT).show()

                    }

                })

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun noticeLoading() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                CalendarObj.api.noticeList(page).enqueue(object:retrofit2.Callback<ResponseNotice>{
                    override fun onResponse(call: Call<ResponseNotice>, response: Response<ResponseNotice>) {
                        if (response.body()!!.code==200){
                            if(!response.body()!!.content.isNullOrEmpty()){
                                setContent(response.body()!!.content)
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseNotice>, t: Throwable) {
                        Toast.makeText(activity, "통신 에러", Toast.LENGTH_SHORT).show()

                    }

                })

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setContent(content: ArrayList<NoticeContent>) {
        modelList.clear()
        if (content.size % 10 != 0 || content.isEmpty()) {
            noMoreItem = true
        }
        for (i in 0 until content.size){
            val title = content[i].title
            val text = content[i].content
            val timeStamp = content[i].timeStamp
            val noticeNo = content[i].notice_no
            val viewCount = content[i].viewCount
            val topFix = content[i].fixTop
            val myModel = NoticeModel(noticeNo.toString(),title,text,viewCount.toString(),timeStamp,topFix)
            modelList.add(myModel)
        }
        noticeRecyclerAdapter.submitList(modelList)

        if(page == 1){
            noticeRecyclerAdapter.notifyDataSetChanged()
        }else{
            noticeRecyclerAdapter.notifyItemRangeInserted((page-1)*10,10)
        }
        ++page
        loadLock = false
    }

}