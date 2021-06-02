package com.capiter.main.util

import com.capiter.main.util.Status.*

data class Resource<out T>(
    val status: Status = SUCCESS,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T? = null, msg: String? = null): Resource<T> {
            return Resource(SUCCESS, data, message = msg)
        }

        fun <T> error(
            msg: String?,
            data: T? = null
        ): Resource<T> {
            return Resource(MESSAGE, data, msg)
        }

        fun <T> loading(data: T?=null): Resource<T> {
            return Resource(LOADING, data)
        }
    }
}

enum class Status {
    SUCCESS,
    MESSAGE,
    LOADING
}