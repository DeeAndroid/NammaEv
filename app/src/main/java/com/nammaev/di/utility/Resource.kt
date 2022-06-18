/*
 * *
 *  * Created by Nethaji on 11/9/21 2:38 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 11/9/21 2:38 PM
 *
 */

package com.nammaev.di.utility

import okhttp3.ResponseBody

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: String = ""
    ) : Resource<Nothing>()

    object Loading : Resource<Nothing>()
}