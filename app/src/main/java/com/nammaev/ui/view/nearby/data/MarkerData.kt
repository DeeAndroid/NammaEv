package com.nammaev.ui.view.nearby.data

import com.nammaev.data.network.api.response.DataItem
import com.nammaev.data.network.api.response.ResStation

data class MarkerData(
    val station: DataItem,
    val lattitude: Double,
    val longitude: Double,
    val avatar: String,
    val selected:Boolean=false
)