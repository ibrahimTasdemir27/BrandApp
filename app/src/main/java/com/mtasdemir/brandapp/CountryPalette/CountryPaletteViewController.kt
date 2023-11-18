package com.mtasdemir.brandapp.CountryPalette

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtasdemir.brandapp.Base.Base.View.BaseViewControllerWithAdMob
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.CountryPalette.Adapter.CountryPaletteCell
import com.mtasdemir.brandapp.CountryPalette.Adapter.CountryPaletteCellDelegate
import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.databinding.CountryPaletteLayoutBinding

class CountryPaletteViewController:
    BaseViewControllerWithAdMob(),
    CountryPaletteViewModelDelegate,
    CountryPaletteCellDelegate {

    private val viewModel = CountryPaletteViewModel()

    private val binding: CountryPaletteLayoutBinding by lazy {
        CountryPaletteLayoutBinding.inflate(layoutInflater)
    }

    override fun provideViewModel(): BaseViewModel? {
        return  viewModel
    }


    override fun createSuccessModifieElements() {
        viewModel.delegate = this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.allCountryList = intent.getParcelableArrayExtra("countryList", BrandModel::class.java)!!
        } else {
            viewModel.allCountryList = intent.getParcelableArrayExtra("countryList") as Array<BrandModel>
        }
    }


    lateinit var leftCountryRecyclerView: RecyclerView
    lateinit var rightCountryRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindItem()
    }

    private fun bindItem() {
        binding.paletteCountryBackButton.setOnClickListener {
            tappedBackButton()
        }

        leftCountryRecyclerView = binding.paletteLeftCountryRecyclerView
        rightCountryRecyclerView = binding.paletteRightCountryRecyclerView

        val leftAdapter = CountryPaletteCell(viewModel.leftCountryModels, true)
        leftAdapter.delegate = this

        leftCountryRecyclerView.adapter = leftAdapter
        leftCountryRecyclerView.layoutManager = LinearLayoutManager(this)



        val rightAdapter = CountryPaletteCell(viewModel.rightCountryModels, false)
        rightAdapter.delegate = this

        rightCountryRecyclerView.adapter = rightAdapter
        rightCountryRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun tappedBackButton() {
        finish()
    }


    /**
     * CountryPaletteCell Delegate
     */
    override fun didSelectPaletteItem(selectedCountry: String, isLeft: Boolean) {
        if (isLeft) {
            viewModel.isSelectedLeftCountry(selectedCountry)
        } else {
            viewModel.isSelectedRightCountry(selectedCountry)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun redCountryContentChanged() {
        val adapter = leftCountryRecyclerView.adapter as CountryPaletteCell
        adapter.countryList = viewModel.leftCountryModels
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun greenCountryContentChanged() {
        val adapter = rightCountryRecyclerView.adapter as CountryPaletteCell
        adapter.countryList = viewModel.rightCountryModels
        adapter.notifyDataSetChanged()

    }







    companion object {

        fun create(allBrandList: Array<BrandModel>): Intent {
            val intent = Intent(appContext, CountryPaletteViewController::class.java)
            intent.putExtra("countryList", allBrandList)

            return intent
        }
    }

}