package com.nammaev.data.network.api.response

import com.google.gson.annotations.SerializedName

data class ResProduct(
    val code: Int? = null,
    @SerializedName("data") val data: List<Product?>? = null,
    val message: String? = null
)

data class Product(
    val price: String? = null,
    val description: String = "",
    val name: String? = null,
    val icon: String? = null,
    @SerializedName("_id") val id: String? = null
)
