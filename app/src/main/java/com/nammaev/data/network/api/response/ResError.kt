package com.nammaev.data.network.api.response

import com.google.gson.annotations.SerializedName


data class ResError(
    @SerializedName("code") val code: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: String? = null
)