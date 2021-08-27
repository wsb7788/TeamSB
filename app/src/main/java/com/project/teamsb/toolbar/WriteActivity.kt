package com.project.teamsb.toolbar

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.teamsb.R
import com.project.teamsb.api.*
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


class WriteActivity:AppCompatActivity(),View.OnClickListener,KeywordRecyclerAdapter.OnItemClickListener{

    private lateinit var binding: ActivityWriteBinding
    lateinit var category:String

    var modelList = ArrayList<KeywordModel>()
    private lateinit var keywordRecyclerAdapter: KeywordRecyclerAdapter


    var keyWord = ArrayList<String>()

    var keywordIndex = 0

    lateinit var id:String

    var no=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, ForcedTerminationService::class.java))

        binding = ActivityWriteBinding.inflate(layoutInflater)

        setContentView(binding.root)

        keywordRecyclerAdapter = KeywordRecyclerAdapter()
        binding.rcvKeyword.apply {
            layoutManager = LinearLayoutManager(this@WriteActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = keywordRecyclerAdapter
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val adapter = ArrayAdapter.createFromResource(this, R.array.category,R.layout.spinner_custom_list)
        adapter.setDropDownViewResource(R.layout.spinner_custom_list2)
        binding.spinner.adapter = adapter

        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        id = pref.getString("id","")!!

      /*  category = if(intent.hasExtra("category")) {
            intent.getStringExtra("category")!! }else{
            "all" }
*/
        category = intent.getStringExtra("category")!!

        when(category){

            "배달"->binding.spinner.setSelection(1)
            "택배"->binding.spinner.setSelection(2)
            "택시"->binding.spinner.setSelection(3)
            "룸메"->binding.spinner.setSelection(4)
            "all" -> binding.spinner.setSelection(0)
            else -> "그럴리가업썽"
        }

        if(intent.hasExtra("edit")){
            no =intent.getIntExtra("no",0)
            setEdit(no)
        }

        binding.btnAddKeyword.setOnClickListener(this)
        keywordRecyclerAdapter.setItemClickListener(this)


    }

    private fun setEdit(no: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ServerObj.api.detail(no).enqueue(object :
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
        binding.etTitle.text = editable
        binding.spinner.setSelection(when(category){
            "배달"->1
            "택배"->2
            "택시"->3
            "룸메"->4
            else -> 0
        })
        binding.etContent.text = null
        editable = SpannableStringBuilder(content.text)

        binding.etContent.text = editable
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
            binding.tvToolbar.text = "수정"
        }else{
            menuInflater.inflate(R.menu.menu_write_toolbar,menu)
            binding.tvToolbar.text = "새 게시글"
        }

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.submit_tb -> {
                when {
                    binding.spinner.selectedItemPosition == 0 -> {
                        Toast.makeText(this, "카테고리를 선택해주세요!",Toast.LENGTH_SHORT).show()
                        return super.onOptionsItemSelected(item)
                    }
                    binding.etTitle.text.isBlank() -> {
                        Toast.makeText(this, "제목을 입력해주세요!",Toast.LENGTH_SHORT).show()
                        return super.onOptionsItemSelected(item)
                    }
                    binding.etContent.text.isBlank() -> {
                        Toast.makeText(this, "내용을 입력해주세요!",Toast.LENGTH_SHORT).show()
                        return super.onOptionsItemSelected(item)
                    }
                    else -> {
                        category = when(binding.spinner.selectedItemPosition){
                            1-> "배달"
                            2-> "택배"
                            3-> "택시"
                            4-> "룸메"
                            else-> ""
                        }
                        submit(id, category)

                    }
                }
            }
            R.id.check_tb -> {
                when {
                    binding.spinner.selectedItemPosition == 0 -> {
                        Toast.makeText(this, "카테고리를 선택해주세요!",Toast.LENGTH_SHORT).show()
                        return super.onOptionsItemSelected(item)
                    }
                    binding.etTitle.text.isBlank() -> {
                        Toast.makeText(this, "제목을 입력해주세요!",Toast.LENGTH_SHORT).show()
                        return super.onOptionsItemSelected(item)
                    }
                    binding.etContent.text.isBlank() -> {
                        Toast.makeText(this, "내용을 입력해주세요!",Toast.LENGTH_SHORT).show()
                        return super.onOptionsItemSelected(item)
                    }else -> {
                    category = when(binding.spinner.selectedItemPosition){
                        1-> "배달"
                        2-> "택배"
                        3-> "택시"
                        4-> "룸메"
                        else-> ""
                    }
                    modify(id,category,no)

                    }

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun modify(id: String, category: String, no: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val title = binding.etTitle.text.toString()
                val text = binding.etContent.text.toString()
                val keyword1 = ArrayList<String>(keyWord)
                ServerObj.api.modifyArticle(id,title,category,text,keyword1,no).enqueue(object: Callback<ResultWrite> {
                    override fun onResponse(call: Call<ResultWrite>, response: Response<ResultWrite>) {
                        if(response.body()!!.check){
                            responseSuccess(response.body()!!.message)
                        }else {
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

    private fun responseSuccess(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun submit(userID: String, category: String){

            CoroutineScope(Dispatchers.IO).launch {
                try{
                    val title = binding.etTitle.text.toString()
                    val text = binding.etContent.text.toString()
                    val keyWord1 = ArrayList<String>(keyWord)
                    ServerObj.api.writeArticle(title,category,userID,text,keyWord1).enqueue(object: Callback<ResultWrite> {
                        override fun onResponse(call: Call<ResultWrite>, response: Response<ResultWrite>) {
                            if (response.body()!!.check){
                                responseSuccess(response.body()!!.message)
                            }else {
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
                var checkText = binding.etKeyword.text
                if(keyWord.size > 2){
                    Toast.makeText(applicationContext, "키워드는 3개까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }else if (checkText.isNullOrBlank()) {
                    Toast.makeText(applicationContext, "키워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }else if(checkText.length>10){
                    Toast.makeText(applicationContext, "10자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }
                else{
                    val text = binding.etKeyword.text.toString()
                    keyWord.add(text)
                    modelList.clear()
                    modelList.add(KeywordModel(text))
                    keywordRecyclerAdapter.submitList(modelList)
                    keywordRecyclerAdapter.notifyDataSetChanged()
                    binding.etKeyword.text = null
                }
            }
        }
    }


    override fun onClick(v: View, position: Int) {
        keyWord.removeAt(position)
        keywordRecyclerAdapter.deleteItem(position)
        keywordRecyclerAdapter.notifyDataSetChanged()
    }


}
