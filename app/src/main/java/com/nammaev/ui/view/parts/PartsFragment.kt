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
import androidx.lifecycle.lifecycleScope
import com.nammaev.R
import com.nammaev.data.network.api.response.Product
import com.nammaev.data.viewmodel.EvViewModel
import com.nammaev.databinding.FragmentPartsBinding
import com.nammaev.di.showToast
import com.nammaev.di.utility.Resource
import com.nammaev.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PartsFragment : Fragment() {

    private val viewModel by viewModel<EvViewModel>()
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
                    requireActivity() showToast "Successfully brought"
                }
            }
        }

        viewModel.getProducts()
        listenForData()
    }

    private fun listenForData() {
        lifecycleScope.launchWhenCreated {
            viewModel.responseProduct.collect { resService ->
                when (resService) {
                    is Resource.Loading -> (activity as MainActivity).blockInput()
                    is Resource.Success -> {
                        resService.value.data?.let {
                            binding?.apply {
                                if (!it.isNullOrEmpty()) {
                                    (rvParts.adapter as EvPartsAdapter).addProducts(it as List<Product>)
                                    tvNoParts.visibility = View.GONE
                                } else {
                                    tvNoParts.visibility = View.VISIBLE
                                    requireActivity() showToast getString(R.string.label_no_parts)
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
        }
    }

}
