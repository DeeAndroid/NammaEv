/*
 * *
 *  * Created by Nethaji on 11/9/21 2:53 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 2:53 PM
 *
 */

package com.nammaev.di.utility

import com.nammaev.di.utility.Constants.ERROR_MSG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface ResponseReceiver {
    suspend fun <T> callApi(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.message().toString())
                    }
                    else -> Resource.Failure(true, null, throwable.message.toString())
                }
            }
        }
    }
}