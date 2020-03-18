package com.eco4ndly.tracorona.infra.di.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Reusable

/**
 * A Sayan Porya code on 2020-02-09
 */

@Suppress("UNCHECKED_CAST")
@Reusable
class ViewModelFactory<V> constructor(
  private val viewModel: V
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return viewModel as T
  }
}