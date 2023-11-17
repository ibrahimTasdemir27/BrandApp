package com.mtasdemir.brandapp.Home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinPrivacySettings
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkSettings
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mtasdemir.brandapp.Alternative.FindAlternativeViewController
import com.mtasdemir.brandapp.Base.Base.View.ADSRewardedTask
import com.mtasdemir.brandapp.Base.Base.View.AlertAction
import com.mtasdemir.brandapp.Base.Base.View.BaseViewControllerWithAdMob
import com.mtasdemir.brandapp.Base.Base.View.BaseViewControllerWithAds
import com.mtasdemir.brandapp.Base.Base.View.BaseViewControllerWithAdsDelegate
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.CountryPalette.CountryPaletteViewController
import com.mtasdemir.brandapp.Detail.DetailBrandViewController
import com.mtasdemir.brandapp.Home.Adapter.BrandCell
import com.mtasdemir.brandapp.Home.Adapter.BrandCellDelegate
import com.mtasdemir.brandapp.Manager.SPrefencesManager
import com.mtasdemir.brandapp.R
import com.mtasdemir.brandapp.databinding.ActivityMainBinding

class HomeViewController :
    BaseViewControllerWithAdMob(),
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

    private lateinit var bannerAdView: AdView
    private lateinit var searchTextField: TextView
    private lateinit var filterButton: Button
    private lateinit var filterButtonTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var findAlternativeButton: Button
    private lateinit var privacyPolicyText: TextView
    private lateinit var termsText: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.delegate = this
        super.onCreate(savedInstanceState)
        modifieElements()
        setContentView(binding.root)
        MobileAds.initialize(this)
        bannerAdView.loadAd(AdRequest.Builder().build())

        createBannerAd()
    }




    private fun modifieElements() {
        bannerAdView = binding.adView
        searchTextField = binding.searchTextField
        filterButton = binding.filterButton
        filterButtonTextView = binding.filterButtonTextView
        recyclerView = binding.recyclerView
        findAlternativeButton = binding.findAlternativeButton
        privacyPolicyText = binding.privacyPolicyText
        termsText = binding.termsText

        recyclerView.layoutManager = LinearLayoutManager(this)
        val emptyArray = ArrayList<BrandModel>()
        val adapter = BrandCell(emptyArray.toTypedArray())
        adapter.delegate = this
        recyclerView.adapter = adapter


        searchTextField.addTextChangedListener {
            textIsChanged()
        }

        binding.selectCountryPaletteTextview.setOnClickListener {
            tappedSelectCountryPalette()
        }

        binding.filterButton.setOnClickListener {
            filterButtonTapped()
        }

        binding.findAlternativeButton.setOnClickListener {
            tappedAlternativeButton()
        }

        binding.termsText.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://doc-hosting.flycricket.io/nerenin-markasi-terms-of-use/ee3d28bd-65ae-4a30-89fd-87ef1dca186f/terms"))
            startActivity(browserIntent)
        }

        binding.privacyPolicyText.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://doc-hosting.flycricket.io/nerenin-markasi-privacy-policy/8630a8ca-9071-459f-95fe-e4ee7ee2a4e8/privacy"))
            startActivity(browserIntent)
        }
    }


    private fun textIsChanged() {
        if (!requestRewardedADS(ADSRewardedTask.searchBrand)) {
            provideRewardedADS(ADSRewardedTask.searchBrand)
            createBannerAd()
            return
        }

        viewModel.filterTextIsChanged(searchTextField.text.toString())
    }

    private fun tappedSelectCountryPalette() {
        println("1")
        startActivity(CountryPaletteViewController.create(viewModel.allList.toTypedArray()))
        println("2")
    }

    private fun tappedAlternativeButton() {
        val intent = FindAlternativeViewController.create(viewModel.allList.toTypedArray())
        startActivity(intent)
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
        val intent = DetailBrandViewController.create(this, brandModel.productName, brandModel)
        startActivity(intent)
    }









    /** HomeViewModel Delegate **/
    @SuppressLint("NotifyDataSetChanged")
    override fun readyFilteredList(list: Array<BrandModel>) {
        val adapter = recyclerView.adapter as BrandCell
        adapter.items = list
        adapter.notifyDataSetChanged()

    }

    override fun getBrandListFailure(err: String) {
        val leftAction = AlertAction("Tamam", completion = {
            viewModel.getBrands()
        })

        val rightAction = AlertAction("Yeniden Dene!", completion = {
            viewModel.getBrands()
        })

        showAlert(title = "Hata ⚠️", message = "Lütfen internet bağlantınızı kontrol edin ve ardından tekrar deneyin",
            leftActions = leftAction, rightActions = rightAction)
    }


    /** Ads Delegate **/

    override fun rewardedSuccess(task: ADSRewardedTask) {
        println("Rewwarded Success")
        SPrefencesManager.timeRestore = System.currentTimeMillis()
    }
    override fun bannerLoaded(banner: MaxAdView) {
        /*
        val rootView = findViewById<LinearLayout>(R.id.home_ads_content)
        rootView.children.forEach {
            rootView.removeView(it)
        }
        println("Banner Loaded")
        rootView.addView(banner)



         */

    }
}