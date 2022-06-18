package com.nammaev.ui.view.nearby

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nammaev.R
import com.nammaev.databinding.FragmentLocationBinding
import com.nammaev.ui.view.nearby.data.MarkerData
import kotlinx.coroutines.*

import com.bumptech.glide.Glide.with

import com.google.android.gms.maps.model.MarkerOptions


class LocationFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentLocationBinding? = null

    var mGoogleMap: GoogleMap? = null
    var mLocationRequest: LocationRequest? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    val markersArray: ArrayList<MarkerData> = ArrayList<MarkerData>()
    var mapmarker: Bitmap? = null
    val hashMapMarker: HashMap<Int, Marker> = HashMap()
 /*   val adapter = UserListAdapter()
    val userOnMapListAdapter=UserOnMapListAdapter()*/

    var marker_for_map:Marker?=null
    var isMapInitiated=false
    var isMapvisble=true
    var islistvisble=false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null)
            binding = FragmentLocationBinding.inflate(layoutInflater, container, false)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        val mapFrag = SupportMapFragment.newInstance()
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.map, mapFrag)
        fragmentTransaction.commit()
        mapFrag.getMapAsync(this)


        markersArray.add(MarkerData(13.0504068,77.7567698,"https://png.pngitem.com/pimgs/s/49-497522_transparent-guy-thinking-png-random-guy-cartoon-png.png","0",false) );
        markersArray.add(MarkerData( 13.047439,77.755986,"https://png.pngitem.com/pimgs/s/49-497669_weegeepedia-cartoon-hd-png-download.png","1",false) );
        markersArray.add(MarkerData(13.047867,77.749581,"https://png.pngitem.com/pimgs/s/49-497724_simple-guy-skills-simple-guy-hd-png-download.png","2",false) );

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                //The last location in the list is the newest
                val location = locationList[locationList.size - 1]
                mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker!!.remove()
                }

                //move map camera
                val latLng = LatLng(location.latitude, location.longitude)
                val cameraPosition =
                    CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude))
                        .zoom(16f).build()
                mGoogleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }


    private fun setMarkerColor(position: Int) {
        val marker: Marker? = hashMapMarker[position]
        marker?.remove()
        hashMapMarker.remove(position)

//        binding.userList.smoothScrollToPosition(position)

        for (i in markersArray.indices) {
            if (i!=position) {
                val marker: Marker? = hashMapMarker[i]
                marker?.remove()
                hashMapMarker.remove(i)
            }

            if (i != position) {
                markersArray.set(
                    i, MarkerData(markersArray[i].lattitude, markersArray[i].longitude, markersArray[i].avatar, markersArray[i].snippets, false))
            }
            else {
                markersArray.set(position, MarkerData(markersArray[position].lattitude, markersArray[position].longitude, markersArray[position].avatar, markersArray[position].snippets, true))
            }

            val markerIcon = getMarkerIcon(
                root = activity?.findViewById(R.id.maplayout) as ViewGroup,
                text = "markerName",
                isSelected = markersArray[i].selected, markersArray[i].avatar)


            marker_for_map = mGoogleMap!!.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            markersArray[i].lattitude,
                            markersArray[i].longitude
                        )
                    )
                    .icon(markerIcon?.let { BitmapDescriptorFactory.fromBitmap(it) })

                //  .icon(markerIcon)
            )
            marker_for_map?.tag = i
            hashMapMarker.put(i, marker_for_map!!)


        }

    }

    private fun checkLocationPermission() {
        permReqLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        ))
    }
    val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                mGoogleMap?.let { onMapReady(it) }
            }
        }



    override fun onPause() {
        super.onPause()

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
        }
    }

    fun getMarkerIcon(root: ViewGroup, text: String?, isSelected: Boolean ,avatar: String): Bitmap? {
        val markerView = CustomMarkerView(root,  isSelected,avatar)
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
        val b = Bitmap.createBitmap(markerView.measuredWidth, markerView.measuredHeight,
            Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        c.translate((-markerView.scrollX).toFloat(), (-markerView.scrollY).toFloat())
        markerView.draw(c)
        return b

    }


    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 120000 // two minute interval

        mLocationRequest!!.fastestInterval = 120000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest!!, mLocationCallback,
                    Looper.myLooper()
                )
                mGoogleMap!!.isMyLocationEnabled = true
                mGoogleMap!!.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(), R.raw.map_style
                    )

                )
                for (i in markersArray.indices) {
                    Log.d("TAG", "onMapReady: $i")
                    val markerIcon = getMarkerIcon(
                        root = activity?.findViewById(R.id.maplayout) as ViewGroup,
                        text = "markerName",
                        isSelected = markersArray[i].selected, markersArray[i].avatar)
                    lifecycleScope.launch {

                        marker_for_map = p0.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        markersArray[i].lattitude,
                                        markersArray[i].longitude
                                    )
                                )
                                .icon(markerIcon?.let { BitmapDescriptorFactory.fromBitmap(it) })

                            //  .icon(markerIcon)
                        )
                        marker_for_map?.tag = i
                        hashMapMarker.put(i, marker_for_map!!)
                    }
                }
                isMapInitiated=true


            } else {
                //Request Location Permission
                checkLocationPermission()
            }
            mGoogleMap?.setOnMarkerClickListener { marker ->

                val position = marker.tag as Int
                setMarkerColor(position)

                false

            }

        } else {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest!!, mLocationCallback,
                Looper.myLooper()
            )
            mGoogleMap!!.isMyLocationEnabled = true
        }
    }

}

private class CustomMarkerView(
    root: ViewGroup,
    isSelected: Boolean,
    avatar: String
) : FrameLayout(root.context) {
    var outline_image: ImageView
    var user_image: ImageView
    var outline_overlay :ImageView

    init {
        View.inflate(context, R.layout.item_map_marker, this)
        outline_image = findViewById(R.id.outline_mage)
        user_image = findViewById(R.id.user_image)
        outline_overlay=findViewById(R.id.outline_overlay)
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        user_image.setImageBitmap(setImages(avatar))
        Log.d("TAG", "isreturning bitmap: ${setImages(avatar)}")

        if (isSelected) {
            outline_overlay.visibility=View.VISIBLE
        } else {
            outline_overlay.visibility=View.GONE
            outline_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_bg_map_bw));
//            user_image.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})

        }
    }


    fun setImages(avatar: String):Bitmap?{
        var bitmap: Bitmap? = null
        runBlocking {
            CoroutineScope(Dispatchers.IO).async {
                bitmap = with(context)
                    .asBitmap()
                    .circleCrop()
                    .load(avatar)
                    .submit()
                    .get()

            }.await()
        }
        Log.d("TAG", "setImages: $bitmap")
        return bitmap
    }

}