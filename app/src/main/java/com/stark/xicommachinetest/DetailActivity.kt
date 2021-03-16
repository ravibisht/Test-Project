package com.stark.xicommachinetest

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.stark.xicommachinetest.databinding.ActivityDetailBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class DetailActivity : AppCompatActivity() {

    private val TAG = "DetailActivity"
    lateinit var binding: ActivityDetailBinding

    companion object {
        var bitmapImageFile: Bitmap? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Detail Screen")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setHomeButtonEnabled(true)

        if (intent != null) {
            val imageUrl = intent.getStringExtra(ImageApiAdapter.IMAGE_URL)

            Glide.with(this).asBitmap().load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bitmapImageFile = resource
                        binding.detailImg.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

            binding.detailSubmitBtn.setOnClickListener { callRetrofit() }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }



    private fun bitmapToImageBodyPart(fileName: String, bitmap: Bitmap): MultipartBody.Part {

        val file = File(this.cacheDir, fileName)
        file.createNewFile()

        val byteArrayStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayStream)
        val bitmapData = byteArrayStream.toByteArray()

        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(file)
            fos?.write(bitmapData)
            fos?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return MultipartBody.Part.createFormData(
            "user_image", file.name,
            RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
            )
        )
    }


    private fun callRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://dev1.xicom.us/xttest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val xicomAPI = retrofit.create(XicomAPI::class.java)

        val responseModel = xicomAPI.submitData(
            RequestBody.create(
                MediaType.parse("text/plain"),
                binding.detailFirstname.text.toString()
            ),
            RequestBody.create(
                MediaType.parse("text/plain"),
                binding.detailLastname.text.toString()
            ),
            RequestBody.create(MediaType.parse("text/plain"), binding.detailEmail.text.toString()),
            RequestBody.create(
                MediaType.parse("text/plain"),
                binding.detailPhoneEt.text.toString()
            ),
            bitmapToImageBodyPart("test1", bitmapImageFile!!)
        )

        responseModel.enqueue(object : Callback<ResponseModel> {

            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@DetailActivity,
                        " ${response.body()?.message} ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.e(TAG, "onFailure: Failed")
            }

        })
    }


}