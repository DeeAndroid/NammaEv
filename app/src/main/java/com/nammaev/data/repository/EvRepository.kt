/*
 * *
 *  * Created by Nethaji on 11/9/21 1:32 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 1:32 PM
 *
 */

package com.nammaev.data.repository

import com.nammaev.data.network.api.service.ServiceApi
import com.nammaev.di.utility.ResponseReceiver

class EvRepository(private val api: ServiceApi) : ResponseReceiver {

    suspend fun getService() = callApi { api.getService() }

    companion object Factory {
        fun create(api: ServiceApi) = EvRepository(api)
    }
}