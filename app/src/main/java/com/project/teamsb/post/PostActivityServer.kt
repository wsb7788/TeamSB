package com.project.teamsb.post


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.R
import com.project.teamsb.databinding.ActivityPostBinding
import org.w3c.dom.Element
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class PostActivityServer : AppCompatActivity(){

    var TAG: String = "로그"

    private lateinit var binding: ActivityPostBinding

    var modelList = ArrayList<CommentModel>()
    private lateinit var commentRecyclerAdapter: CommentRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "MainActivity - onCreate() called")

        var thread = NetworkThread()
        thread.start()


        /*binding.refreshLayout.setOnRefreshListener {

            val myModel = CommentModel(name = "원승빈 키키", profileImage = "https://img1.daumcdn.net/thumb/C100x100.mplusfriend/?fname=http%3A%2F%2Fk.kakaocdn.net%2Fdn%2FIxxPp%2FbtqC9MkM3oH%2FPpvHOkfOiOpKUwvvWcxhJ0%2Fimg_s.jpg", content = "앙기모띠")
            this.modelList.add(myModel)
            commentRecyclerAdapter.submitList(this.modelList)
            commentRecyclerAdapter.notifyDataSetChanged()
            binding.refreshLayout.isRefreshing = false
        }*/
    }

    inner class NetworkThread : Thread() {


        override fun run() {
            try {
                var site =
                    "https://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbstarku22490125001&QueryType=ItemNewAll&MaxResults=3&SearchTarget=Book&start=1&output=xml&version=20131101"
                var url = URL(site)
                var conn = url.openConnection()
                var input = conn.getInputStream()
                var factory = DocumentBuilderFactory.newInstance()
                var builder = factory.newDocumentBuilder()
                var doc = builder.parse(input)

                var root = doc.documentElement

                var item_node_list = root.getElementsByTagName("item")
                Log.d("로그", "${item_node_list.length}")
                for (i in 0 until item_node_list.length) {
                    var itemelement = item_node_list.item(i) as Element
                    var data1nodelist = itemelement.getElementsByTagName("author")
                    var data2nodelist = itemelement.getElementsByTagName("title")
                    var data3nodelist = itemelement.getElementsByTagName("cover")

                    var data1node = data1nodelist.item(0) as Element
                    var data2node = data2nodelist.item(0) as Element
                    var data3node = data3nodelist.item(0) as Element

                    var data1 = data1node.textContent
                    var data2 = data2node.textContent
                    var data3 = data3node.textContent


                    val myModel = CommentModel(name = data1, profileImage = data3, content = data2)

                    modelList.add(myModel)
                }
                commentRecyclerAdapter = CommentRecyclerAdapter()
                commentRecyclerAdapter.submitList(modelList)
                runOnUiThread {
                    binding.rcvComment.apply {
                        layoutManager = LinearLayoutManager(this@PostActivityServer, LinearLayoutManager.VERTICAL, false)
                        adapter = commentRecyclerAdapter
                    } }
                } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}