package com.stark.xicommachinetest

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface XicomAPI {

    @FormUrlEncoded
    @POST("getdata.php")
    fun getImagesByOffset(
        @Field(value = "user_id") userId: Int,
        @Field(value = "offset") offset: Int,
        @Field(value = "type") type: String
    ): Call<ResponseModel>

    @Multipart
    @POST("savedata.php")
    fun submitData(
        @Part("first_name") firstname: RequestBody,
        @Part("last_name") lastname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part user_data: MultipartBody.Part
    ): Call<ResponseModel>
}