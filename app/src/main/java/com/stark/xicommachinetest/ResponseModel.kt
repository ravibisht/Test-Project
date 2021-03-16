package com.stark.xicommachinetest

data class ResponseModel(
    val status: String,
    val message: String,
    var images: ArrayList<Image>
)