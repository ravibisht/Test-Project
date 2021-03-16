package com.stark.xicommachinetest

import com.google.gson.annotations.SerializedName

data class Image(@SerializedName(value = "xt_image") val imageUrl: String, val id: Int)