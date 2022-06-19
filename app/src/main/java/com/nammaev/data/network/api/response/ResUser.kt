package com.nammaev.data.network.api.response

import com.google.gson.annotations.SerializedName

data class ResUser(
    val code: Int? = null,
    @SerializedName("data") val data: ResUserData? = null,
    val message: String? = null
)

data class ResUserData(
    val lastChargeLocation: String = "",
    val rideStateFromLastCharge: String = "",
    val lastCharge: String = "",
    val name: String = "",
    val health: Int = 0,
    val range: String = "",
    @SerializedName("_id") val id: String? = null,
    val vehicle: String = "",
    val isCharging : Boolean = false
)