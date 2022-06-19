package com.nammaev.ui.view.nearby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nammaev.R
import com.nammaev.data.network.api.response.AddRatingRequestBody
import com.nammaev.data.network.api.response.DataItem
import com.nammaev.data.network.api.response.Product
import com.nammaev.data.viewmodel.EvViewModel
import com.nammaev.databinding.FragmentStationDetailsDialogBinding
import com.nammaev.di.showToast
import com.nammaev.di.utility.Resource
import com.nammaev.ui.MainActivity
import com.nammaev.ui.view.parts.EvPartsAdapter
import com.nammaev.ui.view.parts.onSelection
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


typealias onPositionSelection = (position: Int) -> Unit

class StationDetailsDialogFragment(private val position: Int, val onPositionSelection: onPositionSelection, val station: DataItem) : BottomSheetDialogFragment() {

    private val viewModel by sharedViewModel<EvViewModel>()

    private var binding: FragmentStationDetailsDialogBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (binding == null)
           binding = FragmentStationDetailsDialogBinding.inflate(layoutInflater, container, false)

        binding?.price?.text=station.price+"/hr"
        binding?.tvTitle?.text=station.type

        binding?.ratingBar?.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    val rating = binding?.ratingBar?.rating
                    if (rating != null) {
                        if (rating <= 1 || rating <= 5)
                            binding?.btnRate?.background = ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.secondary_btn
                            )
                        else
                            binding?.btnRate?.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.primary_btn)

                    }
                }
            }

            v?.onTouchEvent(event) ?: true
        }

        binding?.btnRate?.setOnClickListener {
            viewModel.addRating(AddRatingRequestBody().apply {
                this.comment = binding?.ratingContent?.toString()
                this.rating = binding?.ratingBar?.rating.toString()
                this.station = ""
            })
        }

        binding?.direction?.setOnClickListener {
            onPositionSelection.invoke(position)
        }

        listenForData()

        return binding?.root
    }

    private fun listenForData() {
        lifecycleScope.launchWhenCreated {
            viewModel.addRating.collect { resService ->
                when (resService) {
                    is Resource.Loading -> (activity as MainActivity).blockInput()
                    is Resource.Success -> {

                    }
                    is Resource.Failure -> {
                        (activity as MainActivity).unblockInput()
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = StationDetailsDialogFragment.javaClass.simpleName
        fun showAddressBottomSheet(
            fragmentManager: FragmentManager,
            position: Int,
            station: DataItem,
            onPositionSelection: onPositionSelection
        ) {
            StationDetailsDialogFragment(position, onPositionSelection, station).show(fragmentManager, TAG)
        }
    }
}