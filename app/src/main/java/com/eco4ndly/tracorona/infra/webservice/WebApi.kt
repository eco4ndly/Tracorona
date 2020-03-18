package com.eco4ndly.tracorona.infra.webservice

import androidx.annotation.WorkerThread
import com.eco4ndly.tracorona.features.models.common.CommonDataClass
import retrofit2.Response
import retrofit2.http.GET

/**
 * A Sayan Porya code on 14/03/20
 */
interface WebApi {
    @WorkerThread
    @GET("/all")
    suspend fun getAllData(): Response<String>

    @WorkerThread
    @GET("/confirmed")
    suspend fun getConfirmedData(): Response<CommonDataClass>

    @WorkerThread
    @GET("/deaths")
    suspend fun getAllDeaths(): Response<CommonDataClass>

    @WorkerThread
    @GET("/recovered")
    suspend fun getAllRecovered(): Response<CommonDataClass>

}