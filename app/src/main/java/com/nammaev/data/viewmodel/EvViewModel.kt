/*
 * *
 *  * Created by Nethaji on 11/9/21 1:34 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 1:34 PM
 *
 */

package com.nammaev.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammaev.data.network.api.requests.RegStation
import com.nammaev.data.network.api.response.ResProduct
import com.nammaev.data.network.api.response.ResReg
import com.nammaev.data.network.api.response.ResStation
import com.nammaev.data.network.api.response.ResUser
import com.nammaev.data.network.api.response.*
import com.nammaev.data.repository.EvRepository
import com.nammaev.di.utility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EvViewModel(private val repository: EvRepository) : ViewModel() {


    private val _responseHomeData: MutableStateFlow<Resource<ResUser>> = MutableStateFlow(Resource.Loading)
    val responseHome: Flow<Resource<ResUser>> get() = _responseHomeData

    private val _stationResponseLiveData:MutableStateFlow<Resource<ResStation>> = MutableStateFlow(Resource.Loading)
    val stationResponseLiveData: Flow<Resource<ResStation>> get() = _stationResponseLiveData

    private val _responseProduct: MutableStateFlow<Resource<ResProduct>> = MutableStateFlow(Resource.Loading)
    val responseProduct: Flow<Resource<ResProduct>> get() = _responseProduct

    private val _addRating: MutableStateFlow<Resource<ResAddRating>> = MutableStateFlow(Resource.Loading)
    val addRating: Flow<Resource<ResAddRating>> get() = _addRating

    private val _responseAddStation: MutableStateFlow<Resource<RegStation>> = MutableStateFlow(Resource.Loading)
    val responseAddStation: Flow<Resource<RegStation>> get() = _responseAddStation

    fun getUser() {
        viewModelScope.launch {
            _responseHomeData.value = Resource.Loading
            _responseHomeData.value = repository.getUser()
        }
    }

    fun getStations() {
        viewModelScope.launch {
            _stationResponseLiveData.value = Resource.Loading
            _stationResponseLiveData.value = repository.getStations()
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            _responseProduct.value = Resource.Loading
            _responseProduct.value = repository.getProducts()
        }
    }

    fun addRating(addRatingRequestBody: AddRatingRequestBody) {
        viewModelScope.launch {
            _addRating.value = Resource.Loading
            _addRating.value = repository.addRating(addRatingRequestBody)
        }
    }

    fun addStations(
       regStation: ResReg
    ) = viewModelScope.launch {
        _responseAddStation.value = Resource.Loading
        _responseAddStation.value = repository.userInfoRepo(regStation)
    }


}