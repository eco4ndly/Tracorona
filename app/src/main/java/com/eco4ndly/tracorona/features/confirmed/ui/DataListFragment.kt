package com.eco4ndly.tracorona.features.confirmed.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eco4ndly.tracorona.R
import com.eco4ndly.tracorona.common.DataType
import com.eco4ndly.tracorona.common.DataType.CONFIRMED
import com.eco4ndly.tracorona.common.DataType.DEATH
import com.eco4ndly.tracorona.common.DataType.RECOVERED
import com.eco4ndly.tracorona.databinding.ListFragmentLayoutBinding
import com.eco4ndly.tracorona.features.base.BaseFragment
import com.eco4ndly.tracorona.features.confirmed.adapter.CoronaDataListAdapter
import com.eco4ndly.tracorona.features.main.CommonViewModel
import com.eco4ndly.tracorona.features.main.DataListFragmentInteracter
import com.eco4ndly.tracorona.features.models.common.CommonDataClass
import com.eco4ndly.tracorona.utils.Utils
import com.eco4ndly.tracorona.utils.extensions.clicks
import com.eco4ndly.tracorona.utils.extensions.exhaustive
import com.eco4ndly.tracorona.utils.extensions.launchBrowserIfUrl
import javax.inject.Inject

/**
 * A Sayan Porya code on 15/03/20
 */
class DataListFragment: BaseFragment(), DataListFragmentInteracter {
  private lateinit var binding: ListFragmentLayoutBinding
  private val dataAdapter: CoronaDataListAdapter by lazy {
    CoronaDataListAdapter()
  }

  companion object {
    private const val ARG_DATA_TYPE = "data_type"
    fun newInstance(dataType: DataType): DataListFragment {
      val bundle = Bundle()
      bundle.putSerializable(ARG_DATA_TYPE, dataType)
      val fragment = DataListFragment()
      fragment.arguments = bundle
      return fragment
    }
  }

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun getRootView(): View {
    binding = ListFragmentLayoutBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun takeOff(bundle: Bundle?) {

    binding.rvData.adapter = dataAdapter

    /*dataType = arguments!!.getSerializable(ARG_DATA_TYPE) as DataType

    when(dataType) {
      CONFIRMED -> commonViewModel.confirmedCaseLiveData().observe(this, observer)
      RECOVERED -> commonViewModel.recoveredCaseLiveData().observe(this, observer)
      DEATH -> commonViewModel.deathCaseLiveData().observe(this, observer)
    }.exhaustive*/

  }

  /*private val observer = Observer<CommonDataClass> {
    binding.tvLastUpdated.text = it.lastUpdated
    binding.tvTotalLatest.text = it.latest.toString()
    dataAdapter.updateList(it.locations)
  }*/


  override fun updateData(data: CommonDataClass) {
    binding.tvLastUpdated.text = getString(R.string.last_updated_on, Utils.parseUTCTime(data.lastUpdated))
    binding.tvTotalLatest.text = getString(R.string.world_wide_total_number, data.latest)
    binding.tvSource.text = getString(R.string.source, data.source)
    binding.tvSource.setOnClickListener {
      data.source.launchBrowserIfUrl(requireContext())
    }
    dataAdapter.updateList(data.locations)
  }

}