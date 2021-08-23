package com.project.teamsb.post



import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.teamsb.R
import com.project.teamsb.api.*
import com.project.teamsb.databinding.ActivityPostBinding
import com.project.teamsb.databinding.DialogReportBinding
import com.project.teamsb.recycler.model.CommentModel
import com.project.teamsb.recycler.adapter.CommentRecyclerAdapter
import com.project.teamsb.toolbar.WriteActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream


class PostActivity : AppCompatActivity(),View.OnClickListener {

    var TAG: String = "로그"

    private lateinit var binding: ActivityPostBinding

    var modelList = ArrayList<CommentModel>()
    private lateinit var commentRecyclerAdapter: CommentRecyclerAdapter
    lateinit var imm: InputMethodManager
    var page: Int = 1
    val index = 20
    var isUserPost = false
    var loadLock = false
    var noMoreItem = false
    var isRefresh = false
    lateinit var curCategory:String
    lateinit var nickname:String
    lateinit var id:String
    var no: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        startService(Intent(this, ForcedTerminationService::class.java))
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager

        no = intent.getIntExtra("no", 0)
        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        id = pref.getString("id","")!!
        nickname = pref.getString("nickname","")!!

        checkMod(id, no)

        commentRecyclerAdapter = CommentRecyclerAdapter()
        binding.rcvComment.apply {
            layoutManager = LinearLayoutManager(this@PostActivity, LinearLayoutManager.VERTICAL, false)
            adapter = commentRecyclerAdapter
        }
        binding.srlPost.setOnRefreshListener {

            if(!loadLock){
                isRefresh = true
                page = 1
                commentRecyclerAdapter.clearList()
                loadLock = true
                accessArticle(no)
            }
            binding.srlPost.isRefreshing = false
        }
        binding.postScrollView.viewTreeObserver.addOnScrollChangedListener {
            var view = binding.postScrollView.getChildAt (binding.postScrollView.childCount - 1);
            var diff =(view.bottom - (binding.postScrollView.height + binding.postScrollView.scrollY));
            if (diff == 0) {
                if (!loadLock&&!noMoreItem) {
                    loadLock = true
                    replyLoading(id,++page,no)
                }
            }
        };
        binding.btnComment.setOnClickListener(this)
    }
    override fun onResume() {
        super.onResume()
            isRefresh = true
            page = 1
            commentRecyclerAdapter.clearList()
            loadLock = true
            noMoreItem = false
        accessArticle(no)


    }

    private fun checkMod(curUser: String, no: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ServerObj.api.checkMod(curUser, no).enqueue(object :
                    Callback<ResultNoReturn>{
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            isUserPost = true
                            invalidateOptionsMenu()
                        }else if(response.body()!!.code == 303){
                            isUserPost = false
                            invalidateOptionsMenu()
                        }
                    }
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun contentLoading(no:Int) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                ServerObj.api.detail(no).enqueue(object :
                    Callback<ResultPost> {
                    override fun onResponse(call: Call<ResultPost>, response: Response<ResultPost>) {
                        if(response.body()!!.check){
                            setContent(response.body()!!.content[0])
                            replyLoading(id, page, no)
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

    private fun accessArticle(no:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ServerObj.api.accessArticle(no).enqueue(object : Callback<ResultNoReturn>{
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            contentLoading(no)

                        }else{
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
    private fun replyLoading(id:String, page: Int, no:Int) {
        binding.progressBar.visibility = VISIBLE
        modelList.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        ServerObj.api.replyList(page, no,id).enqueue(object :
                            Callback<ResultReply> {
                            override fun onResponse(call: Call<ResultReply>, response: Response<ResultReply>) {
                                noMoreItem = response.body()!!.content.size % 20 != 0 || response.body()!!.content.isNullOrEmpty()
                                for (i in response.body()!!.content.indices) {
                                    val nickname = response.body()!!.content[i].userNickname
                                    val content = response.body()!!.content[i].content
                                    val id = id==  response.body()!!.content[i].userId
                                    val timestampBefore =response.body()!!.content[i].timeStamp.substring(5,16) // MM-dd hh:mm 을
                                    val timestamp = timestampBefore.replace("-","/") // MM/dd hh:mm 으로 변경
                                    var stringProfileImage:String
                                    if(response.body()!!.content[i].imageSource.isNullOrBlank()){
                                        stringProfileImage = ""
                                    }else
                                        stringProfileImage = response.body()!!.content[i].imageSource
                                    val byteProfileImage = Base64.decode(stringProfileImage,0)
                                    val inputStream = ByteArrayInputStream(byteProfileImage)
                                    val profileImage = BitmapFactory.decodeStream(inputStream)
                                    val myModel = CommentModel(nickname = nickname,id= id, content = content,timestamp,profileImage)
                                    modelList.add(myModel)
                                }
                                commentRecyclerAdapter.submitList(modelList)
                                if(isRefresh){
                                    commentRecyclerAdapter.notifyDataSetChanged()
                                    isRefresh = false
                                }else{
                                    commentRecyclerAdapter.notifyItemRangeInserted((page -1)*index,response.body()!!.content.size)
                                }
                                loadLock = false
                                binding.progressBar.visibility = INVISIBLE
                            }
                            override fun onFailure(call: Call<ResultReply>, t: Throwable) {
                                Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                                loadLock = false
                                binding.progressBar.visibility = INVISIBLE
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

    }
    override fun onClick(v: View?) {
        when(v){
            binding.btnComment ->{
                var text = binding.etComment.text.toString()
                text.replace(" ", "")
                if(text.isNullOrBlank()){
                    Toast.makeText(this,"댓글을 입력하세요.",Toast.LENGTH_SHORT).show()
                }else{
                    binding.postScrollView.scrollTo(0,0)
                    imm.hideSoftInputFromWindow(v.windowToken,0)
                    uploadComment(no,id)

                }
            }


        }
    }
    private fun uploadComment(no:Int, curUser:String) {
        modelList.clear()
        CoroutineScope(Dispatchers.IO).launch {
            val content = binding.etComment.text.toString()
            try {
                ServerObj.api.writeComment(no, content, curUser).enqueue(object :
                    Callback<ResultNoReturn> {
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            binding.etComment.text.clear()
                            page = 1
                            commentRecyclerAdapter.clearList()
                            isRefresh = true
                            noMoreItem = false
                            Toast.makeText(applicationContext, "댓글 입력 완료.", Toast.LENGTH_SHORT).show()

                            replyLoading(id, page, no)

                        }else{
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()

                        }

                    }
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isUserPost){
            menuInflater.inflate(R.menu.menu_edit,menu)
            menuInflater.inflate(R.menu.menu_delete,menu)
        }else{
            menuInflater.inflate(R.menu.menu_report,menu)
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_tb -> {
                intent = Intent(this, WriteActivity::class.java)
                intent.putExtra("no",no)
                intent.putExtra("edit",true)
                intent.putExtra("category",curCategory)
                startActivity(intent)
            }
            R.id.report_tb -> {
                reportDialog(id,no)
            }
            R.id.delete_tb -> {
                var builder = AlertDialog.Builder(this)
                builder.setTitle("삭제하시겠습니까")
                var listener = DialogInterface.OnClickListener { p0, p1 ->
                    when (p1) {
                        DialogInterface.BUTTON_POSITIVE ->{
                            deleteArticle(id, no)
                            finish()
                        }
                    }
                }
                builder.setPositiveButton("확인", listener)
                builder.setNegativeButton("취소", listener)
                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun reportDialog(id:String, no:Int) {


        val dialogBuilder = AlertDialog.Builder(this)
        val view = DialogReportBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.btnReportPositive.setOnClickListener {
            if(view.spinnerReport.selectedItemPosition == 0 ){
                Toast.makeText(applicationContext, "신고 사유를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else{
                var text = when(view.spinnerReport.selectedItemPosition){
                    1 -> view.spinnerReport.getItemAtPosition(1).toString()
                    2 -> view.spinnerReport.getItemAtPosition(2).toString()
                    3 -> view.spinnerReport.getItemAtPosition(3).toString()
                    else -> "그럴리가없다잉"
                }
                reportArticle(id,no,text)
                alertDialog.onBackPressed()
            }
        }
        view.btnReportNegative.setOnClickListener {
            alertDialog.onBackPressed()
        }
    }

    private fun reportArticle(id: String, no:Int, content:String) {

        CoroutineScope(Dispatchers.Default).launch {
            try {
                ServerObj.api.report(id,no,content).enqueue(object : Callback<ResultNoReturn>{
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "syntax 에러", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun deleteArticle(id: String, no: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ServerObj.api.deleteArticle(id,no).enqueue(object :
                    Callback<ResultNoReturn> {
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.check){
                            Toast.makeText(applicationContext, "삭제완료", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setContent(content: Content){
        binding.tvTitle.text = content.title
        binding.tvToolbar.text = content.category
        binding.tvComment.text = content.replyCount
        curCategory = content.category
        val timestamp = content.timeStamp.substring(5,16) // MM-dd hh:mm 을
        binding.tvTimeStamp.text = timestamp.replace("-","/") // MM/dd hh:mm 으로 변경
            binding.tvNickname.text = content.userNickname
        var hash = ""
        if(content.hash.isNotEmpty()){
            for(i in content.hash){
                hash += "#"
                hash += i
                hash += "     "
            }
        }
        binding.tvKeyword.text = hash
        binding.tvContent.text = content.text

        var stringProfileImage:String
        if(content.imageSource.isNullOrBlank()){
            stringProfileImage = ""
        }else{
            stringProfileImage = content.imageSource
        }
        val byteProfileImage = Base64.decode(stringProfileImage,0)
        val inputStream = ByteArrayInputStream(byteProfileImage)
        val profileImage = BitmapFactory.decodeStream(inputStream)
        Glide
            .with(App.instance)
            .load(profileImage)
            .circleCrop()
            .placeholder(R.drawable.profile_basic)
            .into(binding.ivProfile)
    }

}
