package com.nammaev.ui.view.nearby.interfaces

import com.nammaev.ui.view.nearby.data.MarkerData

interface OnStationClicked {
    fun onStationClicked(id: MarkerData, position: Int)
}