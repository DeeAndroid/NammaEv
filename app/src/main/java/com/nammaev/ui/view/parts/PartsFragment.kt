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
import androidx.lifecycle.LifecycleObserver
import com.nammaev.R
import com.nammaev.data.network.api.response.Services
import com.nammaev.data.viewmodel.EvViewModel
import com.nammaev.databinding.FragmentPartsBinding
import com.nammaev.di.toast
import com.nammaev.di.utility.Resource
import com.nammaev.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PartsFragment : Fragment(), LifecycleObserver {

    private val homeViewModel by sharedViewModel<EvViewModel>()
    private var binding: FragmentPartsBinding? = null
    private val serviceList = mutableListOf<Services>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (binding == null) binding = FragmentPartsBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rvService.adapter = MyServiceAdapter { isAdded, service ->
                if (isAdded)
                    serviceList.add(service)
                else
                    serviceList.remove(service)
            }
        }

        homeViewModel.getServices()
        listenForData()
    }

    private fun listenForData() {
        homeViewModel.responseLiveData.observe(viewLifecycleOwner) { resService ->
            when (resService) {
                is Resource.Loading -> (activity as MainActivity).blockInput()
                is Resource.Success -> {
                    resService.value.data?.let {
                        if (!it.services.isNullOrEmpty())
                            (binding?.rvService?.adapter as MyServiceAdapter).addServiceList(it.services as List<Services>)
                        else
                            context?.toast(getString(R.string.label_no_parts))
                    }
                    (activity as MainActivity).unblockInput()
                    homeViewModel.responseLiveData.removeObservers(this)
                }
                is Resource.Failure -> {
                    (activity as MainActivity).unblockInput()
                }
            }
        }
    }

}
