package com.project.teamsb.toolbar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.project.teamsb.R
import com.project.teamsb.api.NicknameSet
import com.project.teamsb.api.ResultNoReturn
import com.project.teamsb.api.ResultProfileImage
import com.project.teamsb.api.ServerAPI
import com.project.teamsb.databinding.*
import com.project.teamsb.login.LoginActivity
import com.project.teamsb.main.MainActivity
import com.project.teamsb.main.calendar.CalendarObj
import com.project.teamsb.post.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class SettingActivity:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var view: DialogEditProfileImageBinding
    lateinit var profileImage:Bitmap
    lateinit var profileImageBase64:String
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://13.209.10.30:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var serverAPI: ServerAPI = retrofit.create(ServerAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageSet()
        nicknameSet()


        binding.btnEditNickname.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.btnSettingFeedback.setOnClickListener(this)
        binding.ivSettingProfileImage.setOnClickListener(this)
    }

    private fun imageSet() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val id = pref.getString("id","")!!
                CalendarObj.api.getUserProfileImage(id).enqueue(object : Callback<ResultProfileImage>{
                    override fun onResponse(call: Call<ResultProfileImage>, response: Response<ResultProfileImage>) {
                        if(response.body()!!.code == 200){
                            val stringProfileImage:String
                            if(response.body()!!.content.isNullOrBlank()){
                                stringProfileImage = ""
                            }else{
                                stringProfileImage = response.body()!!.content
                            }
                            val byteProfileImage = Base64.decode(stringProfileImage,0)
                            val inputStream = ByteArrayInputStream(byteProfileImage)
                            profileImage = BitmapFactory.decodeStream(inputStream)
                            Glide
                                .with(App.instance)
                                .load(profileImage)
                                .circleCrop()
                                .placeholder(R.drawable.profile_basic)
                                .into(binding.ivSettingProfileImage)
                        }else
                            Toast.makeText(applicationContext,"${response.body()!!.message}",Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResultProfileImage>, t: Throwable) {
                        Toast.makeText(applicationContext,"서버통신 문제",Toast.LENGTH_SHORT).show()
                    }

                })


            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnEditNickname -> {
                nicknameDialog()
            }
            binding.btnLogout -> {
                logoutDialog()
            }
            binding.btnSettingFeedback -> {
                feedbackDialog()
            }
            binding.ivSettingProfileImage -> {
                profileImageDialog()
            }
        }

    }

    private fun feedbackDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = DialogFeedbackBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.btnPositive.setOnClickListener {
            var text = view.tvFeedback.text.toString()
            text.replace(" ", "")
            if(text.isNullOrBlank()){
                Toast.makeText(this,"댓글을 입력하세요.",Toast.LENGTH_SHORT).show()
            }else{
                feedback(view.tvFeedback.text.toString())
                alertDialog.onBackPressed()
            }
        }
        view.btnNegative.setOnClickListener {
            alertDialog.onBackPressed()
        }
    }


    private fun profileImageDialog() {

        val dialogBuilder = AlertDialog.Builder(this)
        view = DialogEditProfileImageBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.btnGallery.setOnClickListener {
            takeAlbum()
        }
        view.btnPositive.setOnClickListener {
            sendProfileImage(profileImageBase64)
            alertDialog.onBackPressed()
        }
        view.btnNegative.setOnClickListener {
            alertDialog.onBackPressed()
        }
    }

    private fun takeAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        startForResult.launch(intent)

    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK) {
                val data = result.data!!.data!!

                val ins = data.let {
                    applicationContext.contentResolver.openInputStream(data)
                }
                val img = BitmapFactory.decodeStream(ins)
                ins?.close()
                val resized = Bitmap.createScaledBitmap(img,300,300,true)
                val byteArrayOutputStream = ByteArrayOutputStream()
                resized.compress(Bitmap.CompressFormat.JPEG, 50,byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val outStream = ByteArrayOutputStream()
                val res = resources
                profileImageBase64 = Base64.encodeToString(byteArray,0)
                //여기까지 인코딩

                val byteProfileImage = Base64.decode(profileImageBase64,0)
                val inputStream = ByteArrayInputStream(byteProfileImage)
                profileImage = BitmapFactory.decodeStream(inputStream)
                Glide
                    .with(App.instance)
                    .load(profileImage)
                    .circleCrop()
                    .placeholder(R.drawable.profile_basic)
                    .into(view.ivEditProfileImage)


            }
            }

    private fun sendProfileImage(image:String) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val id = pref.getString("id","")!!
                CalendarObj.api.profileSet(id,image).enqueue(object : Callback<ResultNoReturn>{
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.code == 200){
                            Glide
                                .with(App.instance)
                                .load(profileImage)
                                .circleCrop()
                                .placeholder(R.drawable.profile_basic)
                                .into(binding.ivSettingProfileImage)
                            Toast.makeText(applicationContext,"이미지 설정 완료",Toast.LENGTH_SHORT).show()
                        }else
                            Toast.makeText(applicationContext,"${response.body()!!.message}",Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext,"서버통신 문제",Toast.LENGTH_SHORT).show()
                    }

                })


            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }


    private fun feedback(content: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val id = pref.getString("id","")!!
                serverAPI.feedback(id, content).enqueue(object : Callback<ResultNoReturn> {
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if (response.body()!!.check) {
                            Toast.makeText(applicationContext, "피드백 감사합니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "${response.body()}", Toast.LENGTH_SHORT).show()
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

    private fun logoutDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = DialogLogoutBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.btnPositive.setOnClickListener {
            deleteToken()

        }
        view.btnNegative.setOnClickListener {
            alertDialog.onBackPressed()
        }
    }

    private fun deleteToken() {

        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        val edit = pref.edit()
        val id = pref.getString("id","")!!

        try{
            serverAPI.getToken(id, "").enqueue(object : Callback<ResultNoReturn> {
                override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                    Toast.makeText(applicationContext,"서버통신 오류",Toast.LENGTH_SHORT).show()

                }

                override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                    if (response.body()!!.check){
                        edit.clear()
                        edit.apply()
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }else{
                        Toast.makeText(applicationContext,"${response.body()!!.message}",Toast.LENGTH_SHORT).show()

                    }
                }
            })
        }catch(e: Exception) {
            e.printStackTrace()
        }

    }

    private fun nicknameDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = DialogEditNicknameBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.btnPositive.setOnClickListener {
            var text = view.etNickname.text.toString()
            text.replace(" ", "")
            if(text.isNullOrBlank()) {
                Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
            }else if(text.length < 2 || text.length > 10){
                Toast.makeText(this, "2~10자의 닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
            }else {
                nicknameMod(text)
                alertDialog.onBackPressed()
            }

        }
        view.btnNegative.setOnClickListener {
            alertDialog.onBackPressed()
        }
    }
    private fun nicknameMod(nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val editUser = pref.edit()
                val id = pref.getString("id","")!!
                serverAPI.nicknameSet(id, nickname).enqueue(object : Callback<NicknameSet> {
                    override fun onResponse(call: Call<NicknameSet>, response: Response<NicknameSet>) {
                        if (response.body()!!.check) {
                            Toast.makeText(applicationContext, "닉네임 변경 완료", Toast.LENGTH_SHORT).show()
                            editUser.putString("nickname",nickname)
                            editUser.apply()
                            nicknameSet()
                        } else {
                            Toast.makeText(applicationContext, "${response.body()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<NicknameSet>, t: Throwable) {
                        Toast.makeText(applicationContext, "통신 에러", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun nicknameSet(){
        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        binding.tvNickname.text = pref.getString("nickname","")
    }
}