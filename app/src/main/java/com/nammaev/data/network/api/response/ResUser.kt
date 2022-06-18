package com.nammaev.data.network.api.response

import com.google.gson.annotations.SerializedName

data class ResUser(
    val code: Int? = null,
    @SerializedName("data") val data: ResUserData? = null,
    val message: String? = null
)

data class ResUserData(
    val lastChargeLocation: String? = null,
    val rideStateFromLastCharge: String? = null,
    val lastCharge: String? = null,
    val name: String? = null,
    val health: String? = null,
    val range: String? = null,
    @SerializedName("_id") val id: String? = null,
    val vehicle: String? = null
)