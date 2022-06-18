/*
 * *
 *  * Created by Nethaji on 12/9/21 11:54 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 12/9/21 11:54 AM
 *
 */

package com.nammaev.ui.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.nammaev.data.viewmodel.EvViewModel
import com.nammaev.databinding.FragmentHomeBinding
import com.nammaev.di.utility.Resource
import com.nammaev.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment(), LifecycleObserver {

    private val homeViewModel by sharedViewModel<EvViewModel>()
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (binding == null) binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getServices()
        listenForData()
    }

    private fun listenForData() {
        homeViewModel.responseLiveData.observe(viewLifecycleOwner) { resService ->
            when (resService) {
                is Resource.Loading -> (activity as MainActivity).blockInput()
                is Resource.Success -> {
                    resService.value.data?.let {

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
