package com.eco4ndly.tracorona.features.models.common


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CommonDataClass(
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("latest")
    val latest: Int,
    @SerializedName("locations")
    var locations: MutableList<Location>,
    @SerializedName("source")
    val source: String
) {
    fun updateLocationData(newLocation: List<Location>) {
        locations.clear()
        locations.addAll(newLocation)
    }
}

@Keep
data class Location(
    @SerializedName("coordinates")
    val coordinates: Coordinates,
    @SerializedName("country")
    val country: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("history")
    val history: Map<String, Int>,
    @SerializedName("latest")
    val latest: Int,
    @SerializedName("province")
    val province: String
)

@Keep
data class Coordinates(
    @SerializedName("lat")
    val lat: String,
    @SerializedName("long")
    val long: String
)