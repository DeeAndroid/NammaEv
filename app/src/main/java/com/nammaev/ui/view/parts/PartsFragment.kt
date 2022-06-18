/*
 * *
 *  * Created by Nethaji on 12/9/21 11:54 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 12/9/21 11:54 AM
 *
 */

package com.nammaev.ui.view.parts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nammaev.data.viewmodel.EvViewModel
import com.nammaev.databinding.FragmentPartsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PartsFragment : Fragment() {

    private val homeViewModel by sharedViewModel<EvViewModel>()
    private var binding: FragmentPartsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (binding == null) binding = FragmentPartsBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rvParts.adapter = EvPartsAdapter { service ->
                AddressBottomSheetDialog.showAddressBottomSheet(childFragmentManager) { address ->

                }
            }
        }

//        homeViewModel.getServices()
        listenForData()
    }

    private fun listenForData() {
        /* lifecycleScope.launchWhenCreated {
             homeViewModel.responseLiveData.collect { resService ->
                 when (resService) {
                     is Resource.Loading -> (activity as MainActivity).blockInput()
                     is Resource.Success -> {
                         resService.value.data?.let {
                             binding?.apply {
                                 if (!it.services.isNullOrEmpty()) {
                                     (rvParts.adapter as EvPartsAdapter).addServiceList(it.services as List<Services>)
                                     tvNoParts.visibility = View.GONE
                                 } else {
                                     tvNoParts.visibility = View.VISIBLE
                                     context?.toast(getString(R.string.label_no_parts))
                                 }
                             }
                         }
                         (activity as MainActivity).unblockInput()
                     }
                     is Resource.Failure -> {
                         binding?.tvNoParts?.visibility = View.VISIBLE
                         (activity as MainActivity).unblockInput()
                     }
                 }
             }
         }*/
    }

}
