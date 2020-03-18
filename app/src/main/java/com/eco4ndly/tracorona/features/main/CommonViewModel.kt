package com.eco4ndly.tracorona.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eco4ndly.tracorona.common.DataType
import com.eco4ndly.tracorona.common.DataType.CONFIRMED
import com.eco4ndly.tracorona.common.DataType.DEATH
import com.eco4ndly.tracorona.common.DataType.RECOVERED
import com.eco4ndly.tracorona.common.SingleLiveEvent
import com.eco4ndly.tracorona.data.api.ApiResult.Error
import com.eco4ndly.tracorona.data.api.ApiResult.Loading
import com.eco4ndly.tracorona.data.api.ApiResult.Success
import com.eco4ndly.tracorona.features.main.repo.CoronaDataRepository
import com.eco4ndly.tracorona.features.models.common.CommonDataClass
import com.eco4ndly.tracorona.features.models.common.Location
import com.eco4ndly.tracorona.utils.extensions.exhaustive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * A Sayan Porya code on 15/03/20
 */
class CommonViewModel @Inject constructor(
    private val repository: CoronaDataRepository
) : ViewModel() {
    /*private val confirmedCaseMutableLiveData: MutableLiveData<CommonDataClass> = MutableLiveData()

    fun confirmedCaseLiveData(): LiveData<CommonDataClass> = confirmedCaseMutableLiveData

    private val deathCaseMutableLiveData: MutableLiveData<CommonDataClass> = MutableLiveData()

    fun deathCaseLiveData(): LiveData<CommonDataClass> = deathCaseMutableLiveData

    private val recoveredCaseMutableLiveData: MutableLiveData<CommonDataClass> = MutableLiveData()

    fun recoveredCaseLiveData(): LiveData<CommonDataClass> = recoveredCaseMutableLiveData*/

    private var confirmedData: CommonDataClass? = null
    private var recoveredData: CommonDataClass? = null
    private var deathData: CommonDataClass? = null

    private val tempLocationList = mutableListOf<Location>()

    private val coronaDataMutableLiveData = MutableLiveData<CommonDataClass>()
    fun getCoronaDataLiveData(): LiveData<CommonDataClass> = coronaDataMutableLiveData

    private val showErrorDialogMutableLiveData = SingleLiveEvent<String>()
    val showErrorDialogLiveData: LiveData<String> = showErrorDialogMutableLiveData

    private val showLoaderMutableLiveData = SingleLiveEvent<Boolean>()
    val showLoaderLiveData: LiveData<Boolean> = showLoaderMutableLiveData

    /*private fun loadCoronaData(dataType: DataType) {
      viewModelScope.launch {
        when (dataType) {
          is CONFIRMED -> {
            repository.getConfirmedCoronaData()
                .collect {
                  when (it) {
                    is Success -> confirmedCaseMutableLiveData.value = it.data
                    is Error -> showErrorDialogMutableLiveData.value = it.message
                    is Loading -> showLoaderMutableLiveData.value = it.isLoading
                  }.exhaustive
                }
          }
          RECOVERED -> {
            repository.getRecoveredCoronaData()
                .collect {
                  when (it) {
                    is Success -> recoveredCaseMutableLiveData.value = it.data
                    is Error -> showErrorDialogMutableLiveData.value = it.message
                    is Loading -> showLoaderMutableLiveData.value = it.isLoading
                  }.exhaustive
                }
          }
          DEATH -> {
            repository.getCoronaDeathData()
                .collect {
                  when (it) {
                    is Success -> deathCaseMutableLiveData.value = it.data
                    is Error -> showErrorDialogMutableLiveData.value = it.message
                    is Loading -> showLoaderMutableLiveData.value = it.isLoading
                  }.exhaustive
                }
          }
        }.exhaustive
      }
    }*/

    fun getCoronaData(dataType: DataType, forceRefresh: Boolean) {
        viewModelScope.launch {
            if (!forceRefresh) {
                delay(400)
                if (dataType is CONFIRMED) {
                    if (confirmedData != null) {
                        coronaDataMutableLiveData.value = confirmedData
                        return@launch
                    }
                } else if (dataType is RECOVERED) {
                    if (recoveredData != null) {
                        coronaDataMutableLiveData.value = recoveredData
                        return@launch
                    }
                } else if (dataType is DEATH) {
                    if (deathData != null) {
                        coronaDataMutableLiveData.value = deathData
                        return@launch
                    }
                }
            }

            repository.getCoronaData(dataType)
                .collect {
                    when (it) {
                        is Success -> {
                            when (dataType) {
                                CONFIRMED -> {
                                    confirmedData = it.data.sort()
                                    coronaDataMutableLiveData.value = confirmedData
                                }
                                RECOVERED -> {
                                    recoveredData = it.data.sort()
                                    coronaDataMutableLiveData.value = recoveredData
                                }
                                DEATH -> {
                                    deathData = it.data.sort()
                                    coronaDataMutableLiveData.value = deathData
                                }
                            }.exhaustive
                        }
                        is Error -> showErrorDialogMutableLiveData.value = it.message
                        is Loading -> showLoaderMutableLiveData.value = it.isLoading
                    }.exhaustive
                }
        }
    }

    fun sortExistingDataByCountryNameAndDispatch() {
        val existingData = coronaDataMutableLiveData.value
        existingData?.let { data ->
            data.locations.sortWith(Comparator { p0, p1 ->
                p1?.country?.toLowerCase()?.let { p0?.country?.toLowerCase()?.compareTo(it) }!!
            })
            coronaDataMutableLiveData.value = data
        }
    }

    fun sortExistingDataByNumberAndDispatch() {
        val existingData = coronaDataMutableLiveData.value
        existingData?.let { data ->
            data.locations.sortWith(Comparator { p0, p1 -> p0?.latest?.let { p1?.latest?.compareTo(it) }!! })
            coronaDataMutableLiveData.value = data
        }
    }

    fun filterExistingDataAndDispatch(textToFilter: String) {
        val existingData = coronaDataMutableLiveData.value

        existingData?.let { data ->
            if (tempLocationList.isEmpty()) {
                tempLocationList.addAll(data.locations)
            }
            val filtered = tempLocationList.filter { location ->
                location.toString().toLowerCase().contains(textToFilter)
            }

            data.updateLocationData(filtered)
            coronaDataMutableLiveData.value = data
        }
    }

    fun invalidateSearchResult() {
        val existingData = coronaDataMutableLiveData.value
        existingData?.apply {
            if (tempLocationList.isNotEmpty()) {
                updateLocationData(tempLocationList)
                tempLocationList.clear()
            }
        }
        coronaDataMutableLiveData.value = existingData
    }

    private fun CommonDataClass.sort(): CommonDataClass {
        locations.sortWith(Comparator { p0, p1 -> p0?.latest?.let { p1?.latest?.compareTo(it) }!! })
        return this
    }

}