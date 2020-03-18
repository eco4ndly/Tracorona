package com.eco4ndly.tracorona.features.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.eco4ndly.tracorona.R
import com.eco4ndly.tracorona.common.DataType.*
import com.eco4ndly.tracorona.databinding.ActivityMainBinding
import com.eco4ndly.tracorona.features.info.InfoActivity
import com.eco4ndly.tracorona.features.main.adapter.ViewPagerAdapter
import com.eco4ndly.tracorona.utils.extensions.*
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    private lateinit var binding: ActivityMainBinding
    private var currentPagePosition = 0
    private val mainScope = MainScope()
    private var isSearchVisible: Boolean = false

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private var fragmentInteracter: DataListFragmentInteracter? = null

    private val viewModel: CommonViewModel by lazy {
        ViewModelProvider(this, vmFactory)[CommonViewModel::class.java]
    }

    private var vpAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivRefresh
            .clicks()
            .onEach {
                askForDataAccordingToPosition(currentPagePosition, true)
            }
            .launchIn(mainScope)

        binding.ivMore
            .clicks()
            .onEach {
                showPopUp(binding.ivMore)
            }
            .launchIn(mainScope)

        binding.ivSearch
            .clicks()
            .onEach {
                if (!isSearchVisible) {
                    binding.llSearchContainer.visible()
                    binding.llTop.gone()
                    binding.etSearch.requestFocus()
                    openSoftKeyboard(binding.etSearch)
                } else {
                    binding.llSearchContainer.gone()
                    binding.llTop.visible()
                }
                isSearchVisible = !isSearchVisible
            }
            .launchIn(mainScope)

        binding.ivBackSearchBox
            .clicks()
            .onEach {
                hideSoftKeyboard()
                binding.llSearchContainer.gone()
                binding.llTop.visible()
                viewModel.invalidateSearchResult()
                isSearchVisible = false
            }
            .launchIn(mainScope)

        binding.etSearch
            .textChanges()
            .debounce(400)
            .onEach {
                viewModel.filterExistingDataAndDispatch(it)
            }
            .launchIn(mainScope)

        setUpViewPager()
        startObservingViewModel()

        viewModel.getCoronaData(CONFIRMED, false)
    }

    override fun onBackPressed() {
        if (isSearchVisible) {
            binding.llSearchContainer.gone()
            binding.llTop.visible()
            isSearchVisible = false
            viewModel.invalidateSearchResult()
        } else {
            super.onBackPressed()
        }
    }

    private fun setUpViewPager() {
        vpAdapter = ViewPagerAdapter(supportFragmentManager)
        binding.vPager.adapter = vpAdapter
        binding.vPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentPagePosition = position
                vpAdapter?.let { adapter ->
                    fragmentInteracter =
                        (adapter.getRegisteredFragment(currentPagePosition) as DataListFragmentInteracter)
                    askForDataAccordingToPosition(position, false)
                }
            }

        })
    }

    private fun askForDataAccordingToPosition(
        pos: Int,
        force: Boolean
    ) {
        when (pos) {
            0 -> {
                viewModel.getCoronaData(CONFIRMED, force)
            }
            1 -> {
                viewModel.getCoronaData(RECOVERED, force)
            }
            2 -> {
                viewModel.getCoronaData(DEATH, force)
            }
        }
    }

    private fun startObservingViewModel() {
        viewModel.showErrorDialogLiveData.observe(this, Observer {
            it?.let { message ->
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton("Ok") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .show()
            }
        })

        viewModel.showLoaderLiveData.observe(this, Observer {
            it?.let {
                if (it) {
                    binding.progress.visible()
                } else {
                    binding.progress.gone()
                }
            }
        })

        viewModel.getCoronaDataLiveData()
            .observe(this, Observer { data ->
                data?.let {
                    if (fragmentInteracter == null && vpAdapter != null) {
                        fragmentInteracter =
                            (vpAdapter!!.getRegisteredFragment(
                                currentPagePosition
                            ) as DataListFragmentInteracter)
                    }
                    fragmentInteracter?.updateData(it)
                }
            })
    }

    private fun showPopUp(view: View) {
        val popup = PopupMenu(this, view)
        val inflater: MenuInflater = popup.menuInflater
        popup.setOnMenuItemClickListener {
            if (it.itemId == R.id.sort_by_num) {
                viewModel.sortExistingDataByNumberAndDispatch()
                return@setOnMenuItemClickListener true
            } else if (it.itemId == R.id.sort_by_country) {
                viewModel.sortExistingDataByCountryNameAndDispatch()
                return@setOnMenuItemClickListener true
            } else if (it.itemId == R.id.info) {
                Intent(this, InfoActivity::class.java).apply {
                    startActivity(this)
                }
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
        inflater.inflate(R.menu.sorting_option_popup_menu, popup.menu)
        popup.show()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}

