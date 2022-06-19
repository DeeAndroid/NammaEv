package com.nammaev.data.network.api.response

import com.google.gson.annotations.SerializedName
import com.nammaev.data.network.api.requests.UserLocation

class ResReg{
    val code: Int? = null
    val message: String? = null
     var type: String = ""
     var availability: String = ""
     var price:String=""
     var address:String=""
     var userLocation: UserLocationReq? = null
}

class UserLocationReq {
    var lat:String= ""
    var lng:String= ""
}