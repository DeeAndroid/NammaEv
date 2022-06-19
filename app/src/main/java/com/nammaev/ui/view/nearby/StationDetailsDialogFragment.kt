package com.nammaev.ui.view.nearby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nammaev.R
import com.nammaev.data.network.api.response.AddRatingRequestBody
import com.nammaev.data.network.api.response.DataItem
import com.nammaev.data.viewmodel.EvViewModel
import com.nammaev.databinding.FragmentStationDetailsDialogBinding
import com.nammaev.di.blockInput
import com.nammaev.di.showToast
import com.nammaev.di.unblockInput
import com.nammaev.di.utility.Resource
import com.nammaev.ui.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


typealias onPositionSelection = (station: DataItem) -> Unit

class StationDetailsDialogFragment(val onPositionSelection: onPositionSelection, val station: DataItem) : BottomSheetDialogFragment() {

    private val viewModel by viewModel<EvViewModel>()

    private var binding: FragmentStationDetailsDialogBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (binding == null)
           binding = FragmentStationDetailsDialogBinding.inflate(layoutInflater, container, false)

        binding?.price?.text= station.price+"/hr"
        binding?.tvTitle?.text= station.type

        binding?.ratingBar?.setOnRatingBarChangeListener { ratingBar, fl, b ->
            if (fl <= 1) {
                binding?.btnRate?.text = getString(R.string.action_report)
                binding?.btnRate?.background = ContextCompat.getDrawable(requireContext(), R.drawable.secondary_btn)
            } else {
                binding?.btnRate?.text = getString(R.string.action_rate)
                binding?.btnRate?.background = ContextCompat.getDrawable(requireContext(), R.drawable.primary_btn)
            }
        }

        listenForData()
        binding?.btnRate?.setOnClickListener {
            requireActivity() blockInput binding?.pbRate
            val req = AddRatingRequestBody(binding?.ratingContent?.text.toString(), station.id, binding?.ratingBar?.rating?.toString(), binding?.ratingBar?.rating!! <= 1)
            viewModel.addRating(req)
        }

        Glide.with(requireContext())
            .load(station?.avatar)
            .circleCrop()
            .into(binding?.userCover!!)

        binding?.direction?.setOnClickListener {
            this.dismiss()
            onPositionSelection.invoke(station)
        }

        return binding?.root
    }

    private fun listenForData() {
        lifecycleScope.launch {
            viewModel.addRating.collect { resService ->
                when (resService) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        (activity as MainActivity).unblockInput()
                        requireActivity() showToast resService.value.message.toString()
                        dismiss()
                    }
                    is Resource.Failure -> {
                        requireActivity() unblockInput  binding?.pbRate
                        (activity as MainActivity).unblockInput()
                        requireActivity() showToast resService.errorBody
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = StationDetailsDialogFragment.javaClass.simpleName
        fun showAddressBottomSheet(
            fragmentManager: FragmentManager,
            station: DataItem,
            onPositionSelection: onPositionSelection
        ) {
            StationDetailsDialogFragment(onPositionSelection, station).show(fragmentManager, TAG)
        }
    }
}