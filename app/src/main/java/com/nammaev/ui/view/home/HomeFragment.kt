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
import androidx.lifecycle.lifecycleScope
import com.nammaev.data.viewmodel.EvViewModel
import com.nammaev.databinding.FragmentHomeBinding
import com.nammaev.di.isSuccess
import com.nammaev.di.showToast
import com.nammaev.di.utility.Resource
import com.nammaev.ui.MainActivity
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : Fragment() {

    private val viewModel by sharedViewModel<EvViewModel>()
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        if (binding == null) binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser()
        listenForData()
    }

    private fun listenForData() {
        lifecycleScope.launchWhenCreated {
            viewModel.responseHome.collect { resUser ->
                when (resUser) {
                    is Resource.Loading -> (activity as MainActivity).blockInput()
                    is Resource.Success -> {
                        if (resUser.value.code?.isSuccess()!!) {
                            resUser.value.data.let {
                                binding?.userData = it
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

}
