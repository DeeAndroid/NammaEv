/*
 * *
 *  * Created by Nethaji on 11/9/21 1:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 1:28 PM
 *
 */

package com.nammaev.data.network.api.service

import com.nammaev.data.network.api.response.ResServices
import com.nammaev.di.utility.API.URL_REQUEST
import retrofit2.Response
import retrofit2.http.GET

interface ServiceApi {

    @GET(URL_REQUEST)
    suspend fun getServices(
//        @Header(AUTHORIZATION) auth: String = "CIGfMA0GCSqGSIb3DQEBAQdqDup1pgSc0tQUMQUAA4GNADCBiQKBgQD3apAg6H2i",
    ): Response<ResServices>

    @GET(URL_REQUEST)
    suspend fun getService(): ResServices
}