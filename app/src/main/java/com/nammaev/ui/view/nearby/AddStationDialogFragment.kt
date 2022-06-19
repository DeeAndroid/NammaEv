package com.nammaev.ui.view.nearby

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nammaev.R
import com.nammaev.databinding.FragmentAddStationDialogBinding
import com.nammaev.databinding.FragmentStationDetailsDialogBinding

class AddStationDialogFragment : Fragment() {

    private lateinit var binding: FragmentAddStationDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddStationDialogBinding.inflate(inflater, container, false)

        binding

        return binding.root
    }

}