/*
 * *
 *  * Created by Nethaji on 12/9/21 11:53 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 12/9/21 11:53 AM
 *
 */

package com.nammaev.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nammaev.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSplashBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(2000)
            val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
          //  action.setSafeArguments("Some Arguments here")
            findNavController().navigate(action)
        }
    }

}
