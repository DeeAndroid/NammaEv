package com.nammaev.data.network.api.response

import com.google.gson.annotations.SerializedName

data class ResStation(
    val code: Int? = null,
    val data: List<DataItem?>? = null,
    @SerializedName("message") val message: String? = null
)

data class DataItem(
    val price: String? = null,
    val location: Location? = null,
    @SerializedName("_id") val id: String? = null,
    val availability: Boolean? = null,
    val avatar: String? = null,
    val type: String? = null
)

data class Location(
    val lng: String? = null,
    @SerializedName("_id") val id: String? = null,
    val lat: String? = null
)