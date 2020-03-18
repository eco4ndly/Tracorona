package com.eco4ndly.tracorona.features.confirmed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eco4ndly.tracorona.R
import com.eco4ndly.tracorona.databinding.ItemLauoutBinding
import com.eco4ndly.tracorona.features.confirmed.adapter.CoronaDataListAdapter.CoronaDataViewHolder
import com.eco4ndly.tracorona.features.models.common.Location
import com.eco4ndly.tracorona.utils.extensions.gone
import com.eco4ndly.tracorona.utils.extensions.visible

/**
 * A Sayan Porya code on 15/03/20
 */
class CoronaDataListAdapter: Adapter<CoronaDataViewHolder>() {
  private val dataList = mutableListOf<Location>()


  fun updateList(newList: List<Location>) {
    newList.run {
      if (isNotEmpty()) {
        dataList.clear()
        dataList.addAll(this)
        this@CoronaDataListAdapter.notifyDataSetChanged()
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CoronaDataViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lauout, parent, false)
    val binding = ItemLauoutBinding.bind(view)
    return CoronaDataViewHolder(binding)
  }

  override fun getItemCount() = dataList.size

  override fun onBindViewHolder(
    holder: CoronaDataViewHolder,
    position: Int
  ) {
    holder.bind(dataList[position])
  }

  inner class CoronaDataViewHolder(private val binding: ItemLauoutBinding): ViewHolder(binding.root) {
    fun bind(data: Location) {
      with(binding) {
        data.let {
          root.context.let { ctx ->
            tvCountry.text = ctx.getString(R.string.country, it.country)
            if (it.province.isNotEmpty()) {
              tvProvince.visible()
              tvProvince.text = ctx.getString(R.string.province, it.province)
            } else {
              tvProvince.gone()
            }
            tvLatestNumber.text = ctx.getString(R.string.latest_num, it.latest)
          }
        }
      }
    }
  }
}