package com.project.teamsb.toolbar

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.R
import com.project.teamsb.api.Content
import com.project.teamsb.api.ResultPost
import com.project.teamsb.api.ResultWrite
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.ActivityWriteBinding
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

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)

    var keyWord = ArrayList<String>()

    var keywordIndex = 0

    lateinit var id:String

    var no=0

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


        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        id = pref.getString("id","")!!

        category = if(intent.hasExtra("category")) {
            intent.getStringExtra("category")!! }else{
            "all" }

        when(category){
            "all" -> binding.spinner.setSelection(0)
            "delivery", "배달"->binding.spinner.setSelection(1)
            "parcel", "택배"->binding.spinner.setSelection(2)
            "taxi","택시"->binding.spinner.setSelection(3)
            "laundry","빨래"->binding.spinner.setSelection(4)
            else -> "그럴리가업썽"
        }
        if(intent.hasExtra("edit")){
            no =intent.getIntExtra("no",0)
            setEdit(no)
        }

        binding.btnAddKeyword.setOnClickListener(this)



    }

    private fun setEdit(no: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                serverAPI.detail(no).enqueue(object :
                    Callback<ResultPost> {
                    override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {
                        if(response.body()!!.check){
                            setContent(response.body()!!.content[0])
                        }
                    }
                    override fun onFailure(call: Call<ResultPost>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
    fun setContent(content: Content){
        var editable: Editable = SpannableStringBuilder(content.title)
        binding.tvTitle.text = editable
        binding.spinner.setSelection(when(category){
            "delivery","배달"->1
            "parcel","택배"->2
            "taxi","택시"->3
            "laundry","빨래"->4
            else -> 0
        })
        editable = SpannableStringBuilder(content.text)
        binding.contentEt.text = editable
        if(content.hash.isNotEmpty()){
            for(i in content.hash){
                keyWord.add(i)
                modelList.add(KeywordModel(i))
            }
            keywordRecyclerAdapter.submitList(modelList)
            keywordRecyclerAdapter.notifyItemRangeChanged(keywordIndex++,1)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(intent.hasExtra("edit")){
            menuInflater.inflate(R.menu.menu_check,menu)
        }else{
            menuInflater.inflate(R.menu.menu_write_toolbar,menu)
        }

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
                    submit(id, category)
                    finish()
                }
            }
            R.id.check_tb -> {
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
                    modify(id,category,no)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun modify(id: String, category: String, no: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val title = binding.tvTitle.text.toString()
                val text = binding.contentEt.text.toString()
                val keyword1 = ArrayList<String>(keyWord)
                serverAPI.modifyArticle(id,title,category,text,keyword1,no).enqueue(object: Callback<ResultWrite> {
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
            }catch (e: Exception){
                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun submit(userID: String, category: String){

            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val title = binding.tvTitle.text.toString()
                    val text = binding.contentEt.text.toString()
                    val keyword1 = ArrayList<String>(keyWord)
                    serverAPI.writeArticle(title,category,userID,text,keyword1).enqueue(object: Callback<ResultWrite> {
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
                }catch (e: Exception){
                    Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                }

            }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnAddKeyword -> {
                if(keyWord.size > 2){
                    Toast.makeText(applicationContext, "키워드는 3개까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }else{
                    val text = binding.etKeyword.text.toString()
                    keyWord.add(text)
                    modelList.clear()
                    modelList.add(KeywordModel(text))
                    keywordRecyclerAdapter.submitList(modelList)
                    keywordRecyclerAdapter.notifyDataSetChanged()
                }
            }

        }
    }


}
