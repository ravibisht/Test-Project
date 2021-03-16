package com.stark.xicommachinetest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.stark.xicommachinetest.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private var mImageList = ArrayList<Image>()

    companion object {
        var offset = 1
    }

    lateinit var adapter: ImageApiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ImageApiAdapter(this)
        binding.imageRv.layoutManager = LinearLayoutManager(this)
        getData(0)
        binding.imageRv.adapter = adapter
        binding.loadMoreBtn.setOnClickListener { getData(++offset) }


    }

    private fun getData(offset: Int = 0) {

        val xicomAPI = Retrofit.Builder()
            .baseUrl("http://dev1.xicom.us/xttest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val imageApiCall =
            xicomAPI.create(XicomAPI::class.java).getImagesByOffset(108, offset, "popular")

        imageApiCall.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse:  Successful")
                    Log.d(TAG, "onResponse: " + response.body()?.status)
                    mImageList.clear()
                    for (image in response.body()?.images!!) {
                        Log.d(TAG, "onResponse: " + image.imageUrl)
                        Log.d(TAG, "onResponse: " + image.id)
                        mImageList.add(image)
                    }
                    adapter.updateImageList(mImageList)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
            }
        })
    }
}