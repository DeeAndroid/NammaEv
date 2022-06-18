/*
 * *
 *  * Created by Nethaji on 11/9/21 1:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 1:28 PM
 *
 */

package com.nammaev.data.network.api.service

import com.nammaev.data.network.api.response.*
import com.nammaev.di.utility.API.ADD_RATING
import com.nammaev.di.utility.API.STATIONS
import com.nammaev.di.utility.API.URL_PRODUCT
import com.nammaev.di.utility.API.URL_USER
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServiceApi {

    @GET(URL_USER)
    suspend fun getUser(): ResUser

    @GET(STATIONS)
    suspend fun getStations():ResStation

    @GET(URL_PRODUCT)
    suspend fun getProducts(): ResProduct

    @POST(ADD_RATING)
    suspend fun addRating(@Body addRatingRequestBody: AddRatingRequestBody): ResAddRating

}