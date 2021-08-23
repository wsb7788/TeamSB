package com.project.teamsb.toolbar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.project.teamsb.R
import com.project.teamsb.api.*
import com.project.teamsb.databinding.*
import com.project.teamsb.login.LoginActivity
import com.project.teamsb.api.ServerObj
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
import java.io.IOException

class SettingActivity:AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var view: DialogEditProfileImageBinding
    lateinit var profileImage: Bitmap
    var profileImageBase64 = "noSet"
    var isImageSet = false
    lateinit var token: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        startService(Intent(this, ForcedTerminationService::class.java))

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("로그", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result!!
        })
        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        setContentView(binding.root)
        binding.switch1.isChecked = !pref.getString("token", null).isNullOrEmpty()

        binding.tvEmail.text = pref.getString("id","")+"@gachon.ac.kr"



        imageSet(binding.ivProfileImage)
        nicknameSet()

        binding.btnEditNickname.setOnClickListener(this)
        binding.btnSettingFeedback.setOnClickListener(this)
        binding.btnProfileImageSet.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.btnPersonalInfo.setOnClickListener(this)
        binding.btnAppInfo.setOnClickListener(this)
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if(NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()){
                    setToken()
                }else{
                    binding.switch1.isChecked = false
                    Snackbar.make(binding.root,"알림 사용을 위해서 알림 권한 설정을 해주세요.",Snackbar.LENGTH_SHORT)
                        .setAction("확인"){
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        .show()
                }

            } else {
                deleteOnlyToken()
                val edit = pref.edit()
                edit.remove("token")
                edit.apply()
            }
        }
    }

    private fun setToken() {
        CoroutineScope(Dispatchers.IO).launch {
            val prefInfo = getSharedPreferences("userInfo", MODE_PRIVATE)
            val edit = prefInfo.edit()
            val id = prefInfo.getString("id", "")!!
            val token = token
            try {
                ServerObj.api.getToken(id, token).enqueue(object : Callback<ResultNoReturn> {
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버통신 오류", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<ResultNoReturn>,
                        response: Response<ResultNoReturn>
                    ) {
                        if (response.body()!!.check) {
                            edit.putString("token", token)
                            edit.apply()
                        } else {
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnEditNickname -> {
                nicknameDialog()
            }
            binding.btnSettingFeedback -> {
                feedbackDialog()
            }
            binding.btnProfileImageSet -> {
                profileImageDialog()
            }
            binding.btnLogout -> {
                logoutDialog()
            }
            binding.btnPersonalInfo -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://summer-echidna-7ed.notion.site/f9a75cbef96d4863bf2bfa9af64e0998")
                )
                startActivity(intent)
            }
            binding.btnAppInfo -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://summer-echidna-7ed.notion.site/8ddb4bc158204cca9c579712101650af")
                )
                startActivity(intent)
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
            if (text.isNullOrBlank()) {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
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
        isImageSet = false
        imageSet(view.ivEditProfileImage)
        view.btnGallery.setOnClickListener {
            val check = isUserGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
            if(check){
                takeAlbum()
            }
        }
        view.btnPrimary.setOnClickListener {
            deleteProfileImage()
            alertDialog.onBackPressed()
        }
        view.btnPositive.setOnClickListener {
            if (isImageSet) {
                sendProfileImage(profileImageBase64)
                alertDialog.onBackPressed()
            } else {
                Toast.makeText(applicationContext, "이미지를 설정해주세요.", Toast.LENGTH_SHORT).show()

            }

        }
        view.btnNegative.setOnClickListener {
            alertDialog.onBackPressed()
        }
    }

    private fun isUserGranted(permissionName: String):Boolean {
        val pref = getSharedPreferences("SettingInfo", MODE_PRIVATE)
        val isFirstCheck = pref.getBoolean("isFirstProfileImageCheck",true)
        val edit = pref.edit()
        if (ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED) {
            return true
        }else{
            if(isFirstCheck){
                edit.putBoolean("isFirstProfileImageCheck",false).apply()
                ActivityCompat.requestPermissions(this, arrayOf(permissionName),0)
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)){
                    // 그냥 거절일 때
                    ActivityCompat.requestPermissions(this, arrayOf(permissionName),0)
                }else { // 다시묻지 않고 거절 눌렀을 때
                    Snackbar.make(view.root,"접근 권한이 필요합니다. 확인을 누르시면 설정으로 이동합니다.",Snackbar.LENGTH_SHORT)
                        .setAction("확인"){
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        .show()
                }
            }
            if (ContextCompat.checkSelfPermission(this, permissionName) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }
    }

    private fun deleteProfileImage() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                val id = pref.getString("id","")!!
                ServerObj.api.profileDelete(id).enqueue(object : Callback<ResultNoReturn>{
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        if(response.body()!!.code == 200){
                            Toast.makeText(applicationContext,"기본 이미지로 변경되었습니다.",Toast.LENGTH_SHORT).show()
                            Glide
                                .with(App.instance)
                                .load("")
                                .circleCrop()
                                .placeholder(R.drawable.profile_basic)
                                .into(binding.ivProfileImage)
                        }
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

                var exif:ExifInterface? = null
                try {
                    exif = ExifInterface(applicationContext.contentResolver.openInputStream(data)!!);
                } catch (e : IOException) {
                    e.printStackTrace();
                }
                val orientation = exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
                val img = BitmapFactory.decodeStream(ins)
                val settedImg = rotateBitmap(img,orientation)
                ins?.close()
                val resized = Bitmap.createScaledBitmap(settedImg!!,settedImg.width/5,settedImg.height/5,true)
                val byteArrayOutputStream = ByteArrayOutputStream()
                resized.compress(Bitmap.CompressFormat.JPEG, 50,byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                profileImageBase64 = Base64.encodeToString(byteArray,0)
                //여기까지 인코딩
                val byteProfileImage = Base64.decode(profileImageBase64,0)
                val inputStream = ByteArrayInputStream(byteProfileImage)
                profileImage = BitmapFactory.decodeStream(inputStream)
                isImageSet = true
                Glide
                    .with(App.instance)
                    .load(profileImage)
                    .circleCrop()
                    .placeholder(R.drawable.profile_basic)
                    .into(view.ivEditProfileImage)
            }
            }
    private fun rotateBitmap(img: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when(orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return img
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> {
                matrix.setScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                matrix.setRotate(180f)
            }

            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.setRotate(-90f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                matrix.setRotate(90f)
            }
            else -> {
                return img
            }
        }
        return try {
            val bmRotated = Bitmap.createBitmap(img,0,0,img.width,img.height,matrix,true)
            img.recycle()
            bmRotated
        }catch (e: OutOfMemoryError){
            e.printStackTrace()
            null
        }
    }
    private fun imageSet(iv:ImageView) {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                    val id = pref.getString("id","")!!
                    ServerObj.api.getUserProfileImage(id).enqueue(object : Callback<ResultProfileImage>{
                        override fun onResponse(call: Call<ResultProfileImage>, response: Response<ResultProfileImage>) {
                            if(response.body()!!.code == 200){
                                var stringProfileImage:String?
                                if(response.body()!!.content.isNullOrBlank()||response.body()!!.content.length<100){
                                    Glide
                                        .with(App.instance)
                                        .load("")
                                        .circleCrop()
                                        .placeholder(R.drawable.profile_basic)
                                        .into(iv)
                                }else{
                                    stringProfileImage = response.body()!!.content
                                    val byteProfileImage = Base64.decode(stringProfileImage,0)
                                    val inputStream = ByteArrayInputStream(byteProfileImage)
                                    profileImage = BitmapFactory.decodeStream(inputStream)
                                    Glide
                                        .with(App.instance)
                                        .load(profileImage)
                                        .circleCrop()
                                        .placeholder(R.drawable.profile_basic)
                                        .into(iv)
                                }
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
    private fun sendProfileImage(image:String) {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
                    val id = pref.getString("id","")!!
                    ServerObj.api.profileSet(id,image).enqueue(object : Callback<ResultNoReturn>{
                        override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                            if(response.body()!!.code == 200){
                                Toast.makeText(applicationContext,"이미지가 설정 되었습니다.",Toast.LENGTH_SHORT).show()
                                Glide
                                    .with(App.instance)
                                    .load(profileImage)
                                    .circleCrop()
                                    .placeholder(R.drawable.profile_basic)
                                    .into(binding.ivProfileImage)
                            }
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
                ServerObj.api.feedback(id, content).enqueue(object : Callback<ResultNoReturn> {
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
            alertDialog.onBackPressed()
        }
        view.btnNegative.setOnClickListener {
            alertDialog.onBackPressed()
        }
    }

    private fun deleteToken() {
        CoroutineScope(Dispatchers.IO).launch {
            val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
            val id = pref.getString("id", "")!!
            try {
                ServerObj.api.getToken(id,null).enqueue(object : Callback<ResultNoReturn> {
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버통신 오류", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                        deleteAndRestart()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun deleteOnlyToken() {
        CoroutineScope(Dispatchers.IO).launch {
            val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
            val id = pref.getString("id", "")!!
            try {
                ServerObj.api.getToken(id,null).enqueue(object : Callback<ResultNoReturn> {
                    override fun onFailure(call: Call<ResultNoReturn>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버통신 오류", Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<ResultNoReturn>, response: Response<ResultNoReturn>) {
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun deleteAndRestart() {
        val pref = getSharedPreferences("userInfo", MODE_PRIVATE)
        val edit = pref.edit()
        edit.clear()
        edit.commit()
        finishAffinity()
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        System.exit(0)
    }


    private fun nicknameDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = DialogEditNicknameBinding.inflate(layoutInflater)
        dialogBuilder.setView(view.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.btnPositive.setOnClickListener {
            var text = view.etNickname.text.toString()
            var textReplaced = text.replace(" ", "")
            if(textReplaced.length != text.length){
                Toast.makeText(this, "공백은 들어갈 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
            else if(textReplaced.isNullOrBlank()) {
                Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()

            }else if(textReplaced.length < 2 || textReplaced.length > 8){
                Toast.makeText(this, "2~8자의 닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
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
                ServerObj.api.nicknameSet(id, nickname).enqueue(object : Callback<NicknameSet> {
                    override fun onResponse(call: Call<NicknameSet>, response: Response<NicknameSet>) {
                        if (response.body()!!.code == 200) {
                            Toast.makeText(applicationContext, "닉네임 변경 완료", Toast.LENGTH_SHORT).show()
                            editUser.putString("nickname",nickname)
                            editUser.apply()
                            nicknameSet()
                        } else {
                            Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
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