/*
 * *
 *  * Created by Nethaji on 11/9/21 9:13 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 9:13 AM
 *
 */

package com.nammaev.di

import com.nammaev.data.network.api.service.ServiceApi
import com.nammaev.data.network.http.HttpClientManager
import com.nammaev.data.network.http.createApi
import com.nammaev.data.repository.EvRepository
import com.nammaev.data.viewmodel.EvViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Networking modules here
 * Must be a singleton
 * Also, using the default overload methods
 **/
val NETWORKING_MODULE = module {
//    single { HttpClientManager.newInstance(androidContext()) }
    single { HttpClientManager.newInstance() }
    single { get<HttpClientManager>().createApi<ServiceApi>() }
}

/**
 * Repository modules here
 * Must be a singleton
 **/
val REPOSITORY_MODULE = module {
    single { EvRepository.create(get()) }
}

/**
 * ViewModel modules here
 **/
val VIEW_MODEL_MODULE = module {
    viewModel { EvViewModel(get()) }
}