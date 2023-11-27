package com.mtasdemir.brandapp.Home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.mtasdemir.brandapp.AddMark.AddMarkViewController
import com.mtasdemir.brandapp.Alternative.FindAlternativeViewController
import com.mtasdemir.brandapp.Base.Base.View.ADSRewardedTask
import com.mtasdemir.brandapp.Base.Base.View.AlertAction
import com.mtasdemir.brandapp.Base.Base.View.BaseViewControllerWithAdMob
import com.mtasdemir.brandapp.Base.Base.View.BaseViewControllerWithAdMobDelegate
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.CountryPalette.CountryPaletteViewController
import com.mtasdemir.brandapp.Detail.DetailBrandViewController
import com.mtasdemir.brandapp.Home.Adapter.BrandCell
import com.mtasdemir.brandapp.Home.Adapter.BrandCellDelegate
import com.mtasdemir.brandapp.Manager.SPrefencesManager
import com.mtasdemir.brandapp.R
import com.mtasdemir.brandapp.databinding.ActivityMainBinding
import com.mtasdemir.brandapp.databinding.AddMarkLayoutBinding

class HomeViewController :
    BaseViewControllerWithAdMob(),
    HomeViewModelDelegate,
    BrandCellDelegate,
    BaseViewControllerWithAdMobDelegate {


    private val viewModel = HomeViewModel()

    override fun provideViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun provideAdsDelegate(): BaseViewControllerWithAdMobDelegate? {
        return this
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var bannerAdView: AdView
    private lateinit var searchTextField: TextView
    private lateinit var filterButton: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var findAlternativeButton: TextView
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

        binding.addNotFoundMark.setOnClickListener {
            tappedNotFoundMarkButton()
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
        startActivity(CountryPaletteViewController.create(viewModel.allList.toTypedArray()))
    }

    private fun tappedAlternativeButton() {
        val intent = FindAlternativeViewController.create(viewModel.allList.toTypedArray())
        startActivity(intent)
    }

    private fun tappedNotFoundMarkButton() {
        val intent = AddMarkViewController.create()
        startActivity(intent)
    }


    @SuppressLint("SetTextI18n")
    private fun filterButtonTapped() {
        val isFilterToBrand = !viewModel.isFilterToBrand

        if (isFilterToBrand) {
            searchTextField.hint = "Marka Adını Yazınız..."
            filterButton.text = "Ülkeye göre"
        } else {
            searchTextField.hint = "Ülke Adını Yazınız..."
            filterButton.text = "Markaya göre"
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
    override fun bannerLoaded(banner: AdView) {
        /*
        val rootView = findViewById<LinearLayout>(R.id.home_ads_content)
        rootView.children.forEach {
            rootView.removeView(it)
        }
        println("Banner Loaded")
        rootView.addView(banner)



         */

    }



    private fun showPopup(point: Point) {

        val layoutInflater =
            appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding = AddMarkLayoutBinding.inflate(layoutInflater)
        val popUp = PopupWindow(appContext)
        popUp.contentView = binding.root
        popUp.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        popUp.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        popUp.isFocusable = true

        val x = 200
        val y = 60
        popUp.setBackgroundDrawable(ColorDrawable())
        popUp.animationStyle = R.style.popup_window_animation
        popUp.showAtLocation(binding.root, Gravity.NO_GRAVITY, point.x , point.y)


    }
}