package com.eco4ndly.tracorona.common

import java.io.Serializable

sealed class DataType: Serializable {
    object CONFIRMED: DataType()
    object RECOVERED: DataType()
    object DEATH: DataType()
  }