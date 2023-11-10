package com.mtasdemir.brandapp.Home

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdk
import com.mtasdemir.brandapp.Alternative.FindAlternativeViewController
import com.mtasdemir.brandapp.Base.Base.View.ADSRewardedTask
import com.mtasdemir.brandapp.Base.Base.View.AlertAction
import com.mtasdemir.brandapp.Base.Base.View.BaseViewControllerWithAds
import com.mtasdemir.brandapp.Base.Base.View.BaseViewControllerWithAdsDelegate
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.Detail.DetailBrandViewController
import com.mtasdemir.brandapp.Home.Adapter.BrandCell
import com.mtasdemir.brandapp.Home.Adapter.BrandCellDelegate
import com.mtasdemir.brandapp.Manager.SPrefencesManager
import com.mtasdemir.brandapp.R
import com.mtasdemir.brandapp.databinding.ActivityMainBinding

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
    lateinit var findAlternativeButton: Button



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
        findAlternativeButton = binding.findAlternativeButton

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

        binding.findAlternativeButton.setOnClickListener {
            tappedAlternativeButton()
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
        SPrefencesManager.timeRestore = System.currentTimeMillis()
    }
    override fun bannerLoaded(banner: MaxAdView) {
        val rootView = findViewById<LinearLayout>(R.id.home_ads_content)
        rootView.children.forEach {
            rootView.removeView(it)
        }
        rootView.addView(banner)
    }
}