package com.project.teamsb.ui.user

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.teamsb.ApplicationClass
import com.project.teamsb.api.ResultPost
import com.project.teamsb.databinding.FragmentUserBinding
import com.project.teamsb.api.ServerObj
import com.project.teamsb.data.remote.user.UserListener
import com.project.teamsb.databinding.FragmentHomeBinding
import com.project.teamsb.post.App
import com.project.teamsb.post.PostActivity
import com.project.teamsb.recycler.adapter.PostRecyclerAdapter
import com.project.teamsb.recycler.model.PostModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class UserFragment : Fragment(), UserListener,PostRecyclerAdapter.OnItemClickListener{

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel:UserViewModel by viewModel()

    var modelList = ArrayList<PostModel>()
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter

    lateinit var id:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUserBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.userListener = this

        recyclerInit()

        id = this!!.requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("id","")!!


        binding.rcvPost.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount-1
                // 스크롤이 끝에 도달했는지 확인
                if (lastVisibleItemPosition == itemTotalCount) {
                    if (!viewModel.loadLock.value!!&&!viewModel.noMoreItem.value!!) {
                        viewModel.loadLock.value = true
                        viewModel.pageplus()
                        userPostLoading()
                    }
                }
            }
        })
        binding.srlCategory.setOnRefreshListener {
            if (!viewModel.loadLock.value!!) {
                viewModel.loadLock.value = true
                viewModel.pageInit()
                userPostLoading()
                viewModel.noMoreItem.value = false

            }
            binding.srlCategory.isRefreshing = false

        }

        binding.clNoPost.visibility = VISIBLE
        postRecyclerAdapter.setItemClickListener(this)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.pageInit()
        viewModel.userPostLoading()
    }

    private fun userPostLoading() {
        binding.progressBar.visibility = VISIBLE
    }
    override fun onClick(v: View, position: Int) {
        val intent = Intent(App.instance, PostActivity::class.java)
        intent.putExtra("no", postRecyclerAdapter.getItemContentNo(position))
        startActivity(intent)
    }

    override fun recyclerInit() {
        postRecyclerAdapter = PostRecyclerAdapter()
        binding.rcvPost.apply {
            layoutManager = GridLayoutManager(ApplicationClass.instance, 2)
            adapter = postRecyclerAdapter
        }
        viewModel.liveData.observe(viewLifecycleOwner,{

            postRecyclerAdapter.submitList(it)
        } )
        viewModel.page.observe(viewLifecycleOwner,{
            if(it == 1) postRecyclerAdapter.clearList()
        })

    }

    override fun apiFailure(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    override fun onLoadingStarted() {
        binding.progressBar.visibility = VISIBLE
    }

    override fun onLoadingEnd() {
        binding.progressBar.visibility = INVISIBLE
    }

    override fun existPost() {
        binding.clNoPost.visibility = INVISIBLE
    }

}