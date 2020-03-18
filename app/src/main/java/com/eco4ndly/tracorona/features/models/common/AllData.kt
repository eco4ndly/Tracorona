package com.eco4ndly.tracorona.features.models.common

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * A Sayan Porya code on 15/03/20
 */

@Keep
data class AllData(
    @SerializedName("confirmed")
    val confirmed: CommonDataClass,
    @SerializedName("deaths")
    val deaths: CommonDataClass,
    @SerializedName("recovered")
    val recovered: CommonDataClass,
    @SerializedName("latest")
    val latestStat: Latest
)

@Keep
data class Latest(
    @SerializedName("confirmed")
    val confirmed: Long,
    @SerializedName("deaths")
    val deaths: Long,
    @SerializedName("recovered")
    val recovered: Long
)