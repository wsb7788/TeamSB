package com.project.teamsb.toolbar

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.R
import com.project.teamsb.api.ResultWrite
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivityWriteBinding
import com.project.teamsb.recycler.adapter.CommentRecyclerAdapter
import com.project.teamsb.recycler.adapter.KeywordRecyclerAdapter
import com.project.teamsb.recycler.model.KeywordModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WriteActivity:AppCompatActivity(),View.OnClickListener{

    private lateinit var binding: ActivityWriteBinding
    lateinit var category:String

    var modelList = ArrayList<KeywordModel>()
    private lateinit var keywordRecyclerAdapter: KeywordRecyclerAdapter

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var writeService: ServerAPI = retrofit.create(ServerAPI::class.java)

    var keyWord = ArrayList<String>()

    var keywordIndex = 0

    private lateinit var commentRecyclerAdapter: CommentRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWriteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        keywordRecyclerAdapter = KeywordRecyclerAdapter()
        binding.rcvKeyword.apply {
            layoutManager = LinearLayoutManager(this@WriteActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = keywordRecyclerAdapter
        }

        setSupportActionBar(binding.toolbar)


        category = if(intent.hasExtra("category")) {
            intent.getStringExtra("category")!! }else{
            "all" }

        when(category){
            "all" -> binding.spinner.setSelection(0)
            "delivery"->binding.spinner.setSelection(1)
            "parcel"->binding.spinner.setSelection(2)
            "taxi"->binding.spinner.setSelection(3)
            "laundry"->binding.spinner.setSelection(4)
        }

        binding.btnAddKeyword.setOnClickListener(this)

        commentRecyclerAdapter = CommentRecyclerAdapter()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write_toolbar,menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.submit_tb -> {
                if(binding.spinner.selectedItemPosition == 0){
                    Toast.makeText(this, "카테고리를 선택해주세요!",Toast.LENGTH_SHORT).show()
                    return super.onOptionsItemSelected(item)
                }else{
                    category = when(binding.spinner.selectedItemPosition){
                        1-> "delivery"
                        2-> "parcel"
                        3-> "taxi"
                        4-> "laundry"
                        else-> ""
                    }
                    submit()
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun submit(){

            CoroutineScope(Dispatchers.Default).launch {
                val title = binding.tvTitle.text.toString()
                val category = category
                val userID = "wsb7788"
                val text = binding.contentEt.text.toString()
                val keyword1 = ArrayList<String>(keyWord)
                writeService.writeArticle(title, category,userID ,text,keyword1).enqueue(object: Callback<ResultWrite> {
                    override fun onResponse(call: Call<ResultWrite>, response: Response<ResultWrite>) {
                        if (response.body()!!.check){
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultWrite>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnAddKeyword -> {
                val text = binding.etKeyword.text.toString()
                keyWord.add(text)
                modelList.clear()
                modelList.add(KeywordModel(text))
                keywordRecyclerAdapter.submitList(modelList)
                keywordRecyclerAdapter.notifyItemRangeChanged(keywordIndex++,1)
            }

        }
    }


}
