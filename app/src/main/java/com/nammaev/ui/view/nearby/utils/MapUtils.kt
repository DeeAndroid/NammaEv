package com.nammaev.ui.view.nearby.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.maps.model.LatLng

object MapUtils {


    fun getOriginDestinationMarkerBitmap(): Bitmap {
        val height = 20
        val width = 20
        val bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }


    /**
     * This function returns the list of locations of Car during the trip i.e. from Origin to Destination
     */
    fun getListOfLocations(): ArrayList<LatLng> {
        val locationList = ArrayList<LatLng>()
        locationList.add(LatLng(28.436970000000002, 77.11272000000001))
        locationList.add(LatLng(28.43635, 77.11289000000001))
        locationList.add(LatLng(28.4353, 77.11317000000001))
        locationList.add(LatLng(28.435280000000002, 77.11332))
        locationList.add(LatLng(28.435350000000003, 77.11368))
        locationList.add(LatLng(28.4356, 77.11498))
        locationList.add(LatLng(28.435660000000002, 77.11519000000001))
        locationList.add(LatLng(28.43568, 77.11521))
        locationList.add(LatLng(28.436580000000003, 77.11499))
        locationList.add(LatLng(28.436590000000002, 77.11507))
        return locationList
    }

}