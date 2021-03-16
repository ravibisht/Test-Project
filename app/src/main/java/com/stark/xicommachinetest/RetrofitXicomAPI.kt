package com.stark.xicommachinetest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitXicomAP() {

    companion object {
        fun create(baseUrl: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }



}


