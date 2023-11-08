package com.example.markaapp.Home

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.example.markaapp.Base.Base.View.ADSRewardedTask
import com.example.markaapp.Base.Base.View.BaseViewControllerWithAds
import com.example.markaapp.Base.Base.View.BaseViewControllerWithAdsDelegate
import com.example.markaapp.Base.Base.View.BaseViewModel
import com.example.markaapp.Detail.DetailBrandViewController
import com.example.markaapp.Home.Adapter.BrandCell
import com.example.markaapp.Home.Adapter.BrandCellDelegate
import com.example.markaapp.Manager.SPrefencesManager
import com.example.markaapp.R
import com.example.markaapp.Service.GoogleImageRequestService
import com.example.markaapp.databinding.ActivityMainBinding
import java.time.Instant
import java.time.LocalDate

class HomeViewController :
    BaseViewControllerWithAds(),
    HomeViewModelDelegate,
    BrandCellDelegate,
    BaseViewControllerWithAdsDelegate {


    private val viewModel = HomeViewModel()

    override fun provideViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun provideAdsDelegate(): BaseViewControllerWithAdsDelegate? {
        return this
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var bannerAd: MaxAdView? = null
    lateinit var bannerContent: LinearLayout
    lateinit var searchTextField: TextView
    lateinit var filterButton: Button
    lateinit var filterButtonTextView: TextView
    lateinit var recyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.delegate = this
        modifieElements()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.getInstance(this).initializeSdk()
        createBannerAd()

    }




    private fun modifieElements() {
        bannerContent = binding.homeAdsContent
        searchTextField = binding.searchTextField
        filterButton = binding.filterButton
        filterButtonTextView = binding.filterButtonTextView
        recyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)
        val emptyArray = ArrayList<BrandModel>()
        val adapter = BrandCell(emptyArray.toTypedArray())
        adapter.delegate = this
        recyclerView.adapter = adapter


        searchTextField.addTextChangedListener {
            textIsChanged()
        }

        binding.filterButton.setOnClickListener {
            filterButtonTapped()
        }

    }


    private fun textIsChanged() {
        if (!requestRewardedADS(ADSRewardedTask.searchBrand)) {
            provideRewardedADS(ADSRewardedTask.searchBrand)
            return
        }

        viewModel.filterTextIsChanged(searchTextField.text.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun filterButtonTapped() {
        val isFilterToBrand = !viewModel.isFilterToBrand

        if (isFilterToBrand) {
            searchTextField.hint = "Marka Adını Yazınız..."
            filterButtonTextView.text = "Ülkeye göre"
        } else {
            searchTextField.hint = "Ülke Adını Yazınız..."
            filterButtonTextView.text = "Markaya göre"
        }

        viewModel.isFilterToBrand = isFilterToBrand
    }


    /** BrandCell Delegate **/
    override fun tappedBrandItem(brandModel: BrandModel) {
        val intent = DetailBrandViewController.create(this, brandModel.name, brandModel)
        startActivity(intent)
    }








    /** HomeViewModel Delegate **/
    @SuppressLint("NotifyDataSetChanged")
    override fun readyFilteredList(list: Array<BrandModel>) {
        println("list is change")
        val adapter = recyclerView.adapter as BrandCell
        adapter.items = list
        adapter.notifyDataSetChanged()

    }

    override fun getBrandListFailure(err: String) {

    }


    /** Ads Delegate **/

    override fun rewardedSuccess(task: ADSRewardedTask) {
        println("Rewarded Success")
        SPrefencesManager.timeRestore = System.currentTimeMillis()
    }
    override fun bannerLoaded(banner: MaxAdView) {
        println("Banner ready show")
        val rootView = findViewById<LinearLayout>(R.id.home_ads_content)
        rootView.children.forEach {
            println("removing $it")
            rootView.removeView(it)
        }
        println("adding $banner")
        rootView.addView(banner)
    }


}