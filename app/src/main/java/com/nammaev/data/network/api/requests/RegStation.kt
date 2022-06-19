package com.nammaev.data.network.api.requests


data class RegStation(
    var type: String = "",
    var availability: String = "",
    var price:String="",
    var address:String="",
    var userLocation: UserLocation?
)


data class UserLocation(
    var lat:String= "",
    var lng:String= ""
)