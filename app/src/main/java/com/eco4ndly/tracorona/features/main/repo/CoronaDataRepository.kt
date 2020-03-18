package com.eco4ndly.tracorona.features.main.repo

import com.eco4ndly.tracorona.common.DataType
import com.eco4ndly.tracorona.data.api.ApiResult
import com.eco4ndly.tracorona.features.models.common.CommonDataClass
import kotlinx.coroutines.flow.Flow

/**
 * A Sayan Porya code on 15/03/20
 */
interface CoronaDataRepository {
  /**
   * Gets the confirmed corona data
   */
  suspend fun getConfirmedCoronaData(): Flow<ApiResult<CommonDataClass>>

  /**
   * Gets the recovered corona data
   */
  suspend fun getRecoveredCoronaData(): Flow<ApiResult<CommonDataClass>>

  /**
   * Gets the corona death data
   */
  suspend fun getCoronaDeathData(): Flow<ApiResult<CommonDataClass>>

  /**
   * Gets the specified data
   * @param dataType The type of data needed
   */
  suspend fun getCoronaData(dataType: DataType): Flow<ApiResult<CommonDataClass>>
}