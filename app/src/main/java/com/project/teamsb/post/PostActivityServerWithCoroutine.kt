package com.project.teamsb.post


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.R
import com.project.teamsb.databinding.ActivityPostBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.w3c.dom.Element
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class PostActivityServerWithCoroutine : AppCompatActivity(){

    var TAG: String = "로그"

    private lateinit var binding: ActivityPostBinding

    var modelList = ArrayList<CommentModel>()
    private lateinit var commentRecyclerAdapter: CommentRecyclerAdapter
    var index = 2
    var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "MainActivity - onCreate() called")

        commentLoading()

 /*       binding.refreshLayout.setOnRefreshListener {


            commentLoading()
            commentRecyclerAdapter.notifyDataSetChanged()

            binding.refreshLayout.isRefreshing = false
        }*/


    }

    private fun commentLoading(){
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.Default).async {
                try {
                    var site =
                        "https://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbstarku22490125001&QueryType=ItemNewAll&MaxResults=$index&SearchTarget=Book&start=$page&output=xml&version=20131101"
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


                        val myModel =
                            CommentModel(name = data1, profileImage = data3, content = data2)

                        modelList.add(myModel)
                    }
                    page += index
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.await()
            commentRecyclerAdapter = CommentRecyclerAdapter()
            commentRecyclerAdapter.submitList(modelList)
                if(index == 2){
                    binding.rcvComment.apply {
                        layoutManager = LinearLayoutManager(this@PostActivityServerWithCoroutine, LinearLayoutManager.VERTICAL, false)
                        adapter = commentRecyclerAdapter
                    }
            }


        }
    }
}