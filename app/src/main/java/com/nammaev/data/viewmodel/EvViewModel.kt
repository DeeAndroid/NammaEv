/*
 * *
 *  * Created by Nethaji on 11/9/21 1:34 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 1:34 PM
 *
 */

package com.nammaev.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammaev.data.network.api.response.ResServices
import com.nammaev.data.repository.EvRepository
import com.nammaev.di.utility.Resource
import kotlinx.coroutines.launch

class EvViewModel(private val repository: EvRepository) : ViewModel() {

    private val _responseLiveData: MutableLiveData<Resource<ResServices>> = MutableLiveData()
    val responseLiveData: LiveData<Resource<ResServices>> get() = _responseLiveData

    fun getServices() {
        viewModelScope.launch {
            _responseLiveData.value = Resource.Loading
            _responseLiveData.value = repository.getService()
        }
    }

}