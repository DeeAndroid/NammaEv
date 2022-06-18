package com.nammaev.data.network.api.response

import com.google.gson.annotations.SerializedName

data class ResStation(

    @field:SerializedName("ResStation")
    val resStation: List<ResStationItem?>? = null,
    val code: Int? = null,
    val message: String? = null,
)

data class Location(

    @field:SerializedName("lng")
    val lng: String? = null,

    @field:SerializedName("lat")
    val lat: String? = null
)

data class ResStationItem(

    @field:SerializedName("distance")
    val distance: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("location")
    val location: Location? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("availability")
    val availability: Boolean? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("avatar")
	val avatar: String? = null
)
