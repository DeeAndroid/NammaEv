package com.nammaev.ui.view.nearby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nammaev.R
import com.nammaev.databinding.FragmentStationDetailsDialogBinding


class StationDetailsDialogFragment : BottomSheetDialogFragment() {

    private var binding: FragmentStationDetailsDialogBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (binding == null)
            binding = FragmentStationDetailsDialogBinding.inflate(layoutInflater, container, false)



        binding?.ratingBar?.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    val rating = binding?.ratingBar?.rating
                    if (rating != null) {
                        if (rating<=1.0)
                            binding?.btnRate?.background = ContextCompat.getDrawable(requireContext(), R.drawable.secondary_btn)
                        else
                            binding?.btnRate?.background = ContextCompat.getDrawable(requireContext(), R.drawable.primary_btn)

                    }
                }

            }

            v?.onTouchEvent(event) ?: true
        }


        return binding?.root
    }

}