/*
 * *
 *  * Created by Nethaji on 11/9/21 1:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 1:28 PM
 *
 */

package com.nammaev.data.network.api.service

import com.nammaev.data.network.api.response.ResProduct
import com.nammaev.data.network.api.response.ResStation
import com.nammaev.data.network.api.response.ResUser
import com.nammaev.di.utility.API.STATIONS
import com.nammaev.di.utility.API.URL_PRODUCT
import com.nammaev.di.utility.API.URL_USER
import retrofit2.http.GET

interface ServiceApi {

    @GET(URL_USER)
    suspend fun getUser(): ResUser

    @GET(STATIONS)
    suspend fun getStations():ResStation

    @GET(URL_PRODUCT)
    suspend fun getProducts(): ResProduct

}