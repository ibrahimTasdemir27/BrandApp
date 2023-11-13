package com.mtasdemir.brandapp.CountryPalette

import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.Manager.SPrefencesManager

interface CountryPaletteViewModelDelegate {
    fun redCountryContentChanged()
    fun greenCountryContentChanged()
}

class CountryPaletteViewModel: BaseViewModel() {

    lateinit var allCountryList: Array<BrandModel>
    lateinit var countryList: Array<String>

    lateinit var leftCountryModels: Array<PaletteCountryCellModel>
    lateinit var rightCountryModels: Array<PaletteCountryCellModel>

    private val redCountrys = SPrefencesManager.redCountrys
    private val greenCountrys = SPrefencesManager.greenCountrys


    var delegate: CountryPaletteViewModelDelegate? = null

    override fun onCreate() {
        countryList = allCountryList.map { it.countryName }.toSet().sorted().toTypedArray()
        leftCountryModels = countryList.map {
            PaletteCountryCellModel(it, redCountrys.contains(it))
        }.toTypedArray()
        rightCountryModels = countryList.map {
            PaletteCountryCellModel(it, greenCountrys.contains(it))
        }.toTypedArray()
        super.onCreate()
    }


    fun isSelectedLeftCountry(country: String) {
        val index = leftCountryModels.indexOfFirst { it.country == country }
        leftCountryModels[index].isSelected = !leftCountryModels[index].isSelected
        SPrefencesManager.redCountrys = leftCountryModels.filter { it.isSelected }.map { it.country }.toTypedArray()
        delegate?.redCountryContentChanged()

        val rightIndex = rightCountryModels.indexOfFirst { it.country == country && it.isSelected }
        if (rightIndex != -1) {
            rightCountryModels[rightIndex].isSelected = false
            SPrefencesManager.greenCountrys = rightCountryModels.filter { it.isSelected }.map { it.country }.toTypedArray()
            delegate?.greenCountryContentChanged()
        }
    }

    fun isSelectedRightCountry(country: String) {
        val index = rightCountryModels.indexOfFirst { it.country == country }
        rightCountryModels[index].isSelected = !rightCountryModels[index].isSelected
        SPrefencesManager.greenCountrys = rightCountryModels.filter { it.isSelected }.map { it.country }.toTypedArray()
        delegate?.greenCountryContentChanged()

        val leftIndex = leftCountryModels.indexOfFirst { it.country == country && it.isSelected }
        if (leftIndex != -1) {
            leftCountryModels[leftIndex].isSelected = false
            SPrefencesManager.redCountrys = leftCountryModels.filter { it.isSelected }.map { it.country }.toTypedArray()
            delegate?.redCountryContentChanged()
        }
    }



}


class PaletteCountryCellModel(
    val country: String,
    var isSelected: Boolean
) {

}