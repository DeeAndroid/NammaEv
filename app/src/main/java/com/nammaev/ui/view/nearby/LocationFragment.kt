package com.nammaev.ui.view.nearby

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
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
import com.bumptech.glide.Glide.with
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nammaev.R
import com.nammaev.data.viewmodel.EvViewModel
import com.nammaev.databinding.FragmentLocationBinding
import com.nammaev.di.isSuccess
import com.nammaev.di.showToast
import com.nammaev.di.utility.Resource
import com.nammaev.ui.MainActivity
import com.nammaev.ui.view.nearby.data.MarkerData
import com.nammaev.ui.view.nearby.interfaces.OnStationClicked
import com.nammaev.ui.view.nearby.utils.AnimationUtils
import com.nammaev.ui.view.nearby.utils.MapUtils.getOriginDestinationMarkerBitmap
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class LocationFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentLocationBinding? = null
    private val viewModel by sharedViewModel<EvViewModel>()

    var mGoogleMap: GoogleMap? = null
    var mLocationRequest: LocationRequest? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var markersArray = mutableListOf<MarkerData>()
    val hashMapMarker: HashMap<Int, Marker> = HashMap()

    var marker_for_map: Marker? = null
    var isMapInitiated = false
    var latLng: LatLng? = null
    private var originMarker: Marker? = null
    private var destinationMarker: Marker? = null
    private var grayPolyline: Polyline? = null
    private var blackPolyline: Polyline? = null
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentLocationBinding.inflate(layoutInflater, container, false)

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

            val mapFrag = SupportMapFragment.newInstance()
            val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.map, mapFrag)
            fragmentTransaction.commit()
            mapFrag.getMapAsync(this)

            binding?.apply {

                userList.adapter = StationsAdapter(object : OnStationClicked {
                    override fun onStationClicked(id: MarkerData, position: Int) {
                        StationDetailsDialogFragment.showAddressBottomSheet(
                            childFragmentManager,
                            position
                        ) {
                            val paths: MutableList<LatLng> = ArrayList<LatLng>()
                            paths.add(0, latLng!!)
                            paths.add(
                                1,
                                LatLng(
                                    markersArray[position].lattitude,
                                    markersArray[position].longitude
                                )
                            )

                            showPath(
                                mutableListOf(
                                    LatLng(latLng!!.latitude, latLng!!.longitude),
                                    LatLng(
                                        markersArray[position].lattitude,
                                        markersArray[position].longitude
                                    )
                                )
                            )
                        }
                    }
                })

                userList.addOnItemChangedListener { _, adapterPosition ->
                    if (markersArray.size <= adapterPosition) return@addOnItemChangedListener
                    marker_for_map?.showInfoWindow()
                    mGoogleMap?.animateCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                markersArray[adapterPosition].lattitude,
                                markersArray[adapterPosition].longitude
                            )
                        ), 250, null
                    )
                    if (isMapInitiated) setMarkerColor(adapterPosition)
                }

                btnAddStation.setOnClickListener {
                    btnAddStation.visibility = View.GONE
                    userList.visibility = View.GONE
                    viewAddStation.root.visibility = View.VISIBLE
                }

                viewAddStation.close.setOnClickListener {
                    btnAddStation.visibility = View.VISIBLE
                    userList.visibility = View.VISIBLE
                    viewAddStation.root.visibility = View.GONE
                }

                viewAddStation.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.rbHome -> {
                            viewAddStation.group.isEnabled = true
                        }
                        R.id.rbStation -> {
                            viewAddStation.group.isEnabled = true
                        }
                        R.id.rbRepair -> {
                            viewAddStation.group.isEnabled = false
                        }
                    }
                }

            }

            listenForData()
            viewModel.getStations()
        }
        return binding?.root
    }

    private fun listenForData() {
        lifecycleScope.launchWhenCreated {
            viewModel.stationResponseLiveData.collect { resUser ->
                when (resUser) {
                    is Resource.Loading -> (activity as MainActivity).blockInput()
                    is Resource.Success -> {
                        if (resUser.value.code?.isSuccess()!!) {
                            markersArray.clear()
                            resUser.value.data?.forEachIndexed { index, resStationItem ->
                                markersArray.add(
                                    index, MarkerData(
                                        resStationItem?.location?.lat?.toDouble()!!,
                                        resStationItem.location.lng?.toDouble()!!,
                                        resStationItem.avatar.toString(),
                                        false
                                    )
                                )
                            }
                            (binding?.userList?.adapter as StationsAdapter).setModelArrayList(markersArray)
                            if (!markersArray.isNullOrEmpty()) {
                                addMarkers(mGoogleMap)
                                mGoogleMap?.animateCamera(
                                    CameraUpdateFactory.newLatLng(
                                        LatLng(
                                            markersArray[0].lattitude,
                                            markersArray[0].longitude
                                        )
                                    ), 250, null
                                )
                            }
                        } else
                            requireActivity() showToast resUser.value.message.toString()
                        (activity as MainActivity).unblockInput()
                    }
                    is Resource.Failure -> {
                        requireActivity() showToast resUser.errorBody
                        (activity as MainActivity).unblockInput()
                    }
                }
            }
        }
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
                latLng = LatLng(location.latitude, location.longitude)
                val cameraPosition =
                    CameraPosition.Builder().target(LatLng(latLng?.latitude!!, latLng?.longitude!!))
                        .zoom(16f).build()
                mGoogleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                binding?.viewAddStation?.etAddress?.text =
                    getCompleteAddress(latLng?.latitude!!, latLng?.longitude!!).trim().toEditable()
            }
        }
    }

    private fun getCompleteAddress(LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            val returnedAddress: Address = addresses[0]
            val strReturnedAddress = StringBuilder("")
            for (i in 0..returnedAddress.maxAddressLineIndex) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
            }
            strAdd = strReturnedAddress.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return strAdd
    }

    private fun setMarkerColor(position: Int) {
        val marker: Marker? = hashMapMarker[position]
        marker?.remove()
        hashMapMarker.remove(position)

        binding?.userList?.smoothScrollToPosition(position)

        for (i in markersArray.indices) {
            if (i != position) {
                val marker: Marker? = hashMapMarker[i]
                marker?.remove()
                hashMapMarker.remove(i)
            }

            if (i != position) {
                markersArray.set(
                    i,
                    MarkerData(
                        markersArray[i].lattitude,
                        markersArray[i].longitude,
                        markersArray[i].avatar,
                        false
                    )
                )
            } else {
                markersArray.set(
                    position,
                    MarkerData(
                        markersArray[position].lattitude,
                        markersArray[position].longitude,
                        markersArray[position].avatar,
                        true
                    )
                )
            }

            val markerIcon = getMarkerIcon(
                root = activity?.findViewById(R.id.maplayout) as ViewGroup,
                text = "markerName",
                isSelected = markersArray[i].selected, markersArray[i].avatar
            )


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
        permReqLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
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

    fun getMarkerIcon(
        root: ViewGroup,
        text: String?,
        isSelected: Boolean,
        avatar: String
    ): Bitmap? {
        val markerView = CustomMarkerView(root, isSelected, avatar)
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)
        val b = Bitmap.createBitmap(
            markerView.measuredWidth, markerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
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
                addMarkers(p0)
                isMapInitiated = true


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

    private fun addMarkers(p0: GoogleMap?) {
        for (i in markersArray.indices) {
            Log.d("TAG", "onMapReady: $i")
            val marker: View = requireActivity().findViewById(R.id.maplayout) ?: return

            val markerIcon = getMarkerIcon(
                root = marker as ViewGroup,
                text = "markerName",
                isSelected = markersArray[i].selected, markersArray[i].avatar
            )
            lifecycleScope.launch {

                marker_for_map = p0?.addMarker(
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
    }

    private fun showPath(latLngList: MutableList<LatLng>) {
        val builder = LatLngBounds.Builder()
        for (latLng in latLngList) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 2))

        val polylineOptions = PolylineOptions()
        polylineOptions.color(Color.GRAY)
        polylineOptions.width(5f)
        polylineOptions.addAll(latLngList)
        grayPolyline = mGoogleMap?.addPolyline(polylineOptions)

        val blackPolylineOptions = PolylineOptions()
        blackPolylineOptions.color(Color.BLACK)
        blackPolylineOptions.width(5f)
        blackPolyline = mGoogleMap?.addPolyline(blackPolylineOptions)

        originMarker = addOriginDestinationMarkerAndGet(latLngList[0])
        originMarker?.setAnchor(0.5f, 0.5f)
        destinationMarker = addOriginDestinationMarkerAndGet(latLngList[latLngList.size - 1])
        destinationMarker?.setAnchor(0.5f, 0.5f)

        val polylineAnimator = AnimationUtils.polylineAnimator()
        polylineAnimator.addUpdateListener { valueAnimator ->
            val percentValue = (valueAnimator.animatedValue as Int)
            val index = (grayPolyline?.points!!.size) * (percentValue / 100.0f).toInt()
            blackPolyline?.points = grayPolyline?.points!!.subList(0, index)
        }
        polylineAnimator.start()
    }

    private fun addOriginDestinationMarkerAndGet(latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(getOriginDestinationMarkerBitmap())
        return mGoogleMap?.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )!!
    }

}

private class CustomMarkerView(
    root: ViewGroup,
    isSelected: Boolean,
    avatar: String
) : FrameLayout(root.context) {
    var outline_image: ImageView
    var user_image: ImageView
    var outline_overlay: ImageView

    init {
        View.inflate(context, R.layout.item_map_marker, this)
        outline_image = findViewById(R.id.outline_mage)
        user_image = findViewById(R.id.user_image)
        outline_overlay = findViewById(R.id.outline_overlay)
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        user_image.setImageBitmap(setImages(avatar))
        Log.d("TAG", "isreturning bitmap: ${setImages(avatar)}")

        if (isSelected) {
            outline_overlay.visibility = View.VISIBLE
        } else {
            outline_overlay.visibility = View.GONE
            outline_image.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.circle_bg_map_bw
                )
            )
//            user_image.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f)})

        }
    }

    fun setImages(avatar: String): Bitmap? {
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
        return bitmap
    }

}