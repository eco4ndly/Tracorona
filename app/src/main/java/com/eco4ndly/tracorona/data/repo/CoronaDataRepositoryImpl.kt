package com.eco4ndly.tracorona.data.repo

import com.eco4ndly.tracorona.common.DataType
import com.eco4ndly.tracorona.common.DataType.CONFIRMED
import com.eco4ndly.tracorona.common.DataType.DEATH
import com.eco4ndly.tracorona.common.DataType.RECOVERED
import com.eco4ndly.tracorona.data.api.ApiResult
import com.eco4ndly.tracorona.data.api.ErrorHandler
import com.eco4ndly.tracorona.data.api.ErrorResponse
import com.eco4ndly.tracorona.features.main.repo.CoronaDataRepository
import com.eco4ndly.tracorona.features.models.common.CommonDataClass
import com.eco4ndly.tracorona.infra.webservice.WebApi
import com.eco4ndly.tracorona.utils.exception.NoDataException
import com.eco4ndly.tracorona.utils.extensions.applyCommonStuffs
import com.eco4ndly.tracorona.utils.extensions.exhaustive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class CoronaDataRepositoryImpl @Inject constructor(
  private val webApi: WebApi
) : CoronaDataRepository {

  override suspend fun getConfirmedCoronaData() = flow {
    emit(_getCoronaData(CONFIRMED))
  }
      .applyCommonStuffs()
      .catch { exception ->
        Timber.e(exception)
        emit(ApiResult.Error(exception))
      }

  override suspend fun getRecoveredCoronaData() = flow {
    emit(_getCoronaData(RECOVERED))
  }
  .applyCommonStuffs()
  .catch { exception ->
    Timber.e(exception)
    emit(ApiResult.Error(exception))
  }

  override suspend fun getCoronaDeathData() = flow {
    emit(_getCoronaData(DEATH))
  }
      .applyCommonStuffs()
      .catch { exception ->
        Timber.e(exception)
        emit(ApiResult.Error(exception))
      }

  override suspend fun getCoronaData(dataType: DataType): Flow<ApiResult<CommonDataClass>> {
    return when(dataType) {
      CONFIRMED -> getConfirmedCoronaData()
      RECOVERED -> getRecoveredCoronaData()
      DEATH -> getCoronaDeathData()
    }.exhaustive
  }

  private suspend fun _getCoronaData(dataType: DataType): ApiResult<CommonDataClass> {
    return when(dataType) {
      is CONFIRMED -> {
        webApi.getConfirmedData().doStuff()
      }
      RECOVERED -> {
        webApi.getAllRecovered().doStuff()
      }
      DEATH -> {
        webApi.getAllDeaths().doStuff()
      }
    }
  }

  private fun Response<CommonDataClass>.doStuff(): ApiResult<CommonDataClass> {
    return run {
      if (isSuccessful && body() != null) {
        ApiResult.Success(body()!!)
      } else {
        ApiResult.Error(
            NoDataException(
                ErrorHandler.parseError<ErrorResponse>(errorBody())?.message
            )
        )
      }
    }
  }
}