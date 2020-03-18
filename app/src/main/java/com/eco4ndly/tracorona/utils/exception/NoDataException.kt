package com.eco4ndly.tracorona.utils.exception

import com.eco4ndly.tracorona.data.api.ErrorHandler

/**
 * A Sayan Porya code on 2020-02-08
 */
class NoDataException(message: String? = ErrorHandler.UNKNOWN_ERROR) : Throwable(message)