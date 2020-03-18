package com.eco4ndly.tracorona.features.main.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.eco4ndly.tracorona.common.DataType.CONFIRMED
import com.eco4ndly.tracorona.common.DataType.DEATH
import com.eco4ndly.tracorona.common.DataType.RECOVERED
import com.eco4ndly.tracorona.features.confirmed.ui.DataListFragment
import com.eco4ndly.tracorona.utils.SmartFragmentStatePagerAdapter

/**
 * A Sayan Porya code on 15/03/20
 */
class ViewPagerAdapter(fragmentManager: FragmentManager) : SmartFragmentStatePagerAdapter(
    fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

  override fun getItem(position: Int): Fragment {
    return when (position) {
      0 -> {
        DataListFragment.newInstance(CONFIRMED)
      }
      1 -> {
        DataListFragment.newInstance(RECOVERED)
      }
      2 -> {
        DataListFragment.newInstance(DEATH)
      }
      else -> Fragment()
    }
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return when(position) {
      0 -> {
        "Confirmed"
      }
      1 -> {
        "Recovered"
      }
      2 -> {
        "Death"
      }
      else -> "Undefiled"
    }
  }

  override fun getCount() = 3
}