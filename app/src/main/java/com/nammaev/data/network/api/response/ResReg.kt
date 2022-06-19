package com.nammaev.data.network.api.response

data class ResReg(
    var type: String = "",
    var availability: Boolean = false,
    var price: String = "",
    var address: String = "",
    var location: UserLocationReq? = null,
    val avatar: String = ""
)

data class UserLocationReq(
    var lat: String = "",
    var lng: String = ""
)