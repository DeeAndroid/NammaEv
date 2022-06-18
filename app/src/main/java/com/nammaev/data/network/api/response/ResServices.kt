package com.nammaev.data.network.api.response

import com.google.gson.annotations.SerializedName

data class ResServices(
    @SerializedName("data") val data: Data? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: Boolean
)

data class Data(
    @SerializedName("category_name") val categoryName: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("services") val services: List<Services?>? = null
)

data class Services(
    @SerializedName("rate") val rate: Int? = null,
    @SerializedName("service_name") val serviceName: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("description") val description: String? = null,
    var isAdded: Boolean = false  //  For internal logic
)
