package com.mtasdemir.brandapp.Alternative

import com.mtasdemir.brandapp.Alternative.Adapter.AlternativeRecyclerType
import com.mtasdemir.brandapp.Base.Base.BaseError
import com.mtasdemir.brandapp.Base.Base.Protocols.Firebase.executeOnlyArray
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.Service.FDBReadService
import kotlinx.coroutines.runBlocking

interface FindAlternativeViewModelDelegate {
    fun leftCountrContentReady(list: Array<String>)
    fun rightCountryContentReady(list: Array<String>)
    fun changedSector(sector: String)
    fun getContentFailure(failure: BaseError)
}


class FindAlternativeViewModel: BaseViewModel() {


    //var allList = BrandModel.sampleBrands
    lateinit var allList: Array<BrandModel>

    private var currentCategory: String = "YİYECEK İCECEK"
        set(value) {
            field = value
            delegate?.changedSector(value)
            changedSector(value)
        }

    var leftCountry: String = "İsrail"
        set(value) {
            field = value
            delegate?.leftCountrContentReady(provideCountryProducts(value))

        }

    var rightCountry: String = "Türkiye"
        set(value) {
            field = value
            delegate?.rightCountryContentReady(provideCountryProducts(value))
        }


    var delegate: FindAlternativeViewModelDelegate? = null


    override fun onCreate() {
        super.onCreate()
       // getBrands()
    }



    private fun provideCountryProducts(country: String): Array<String> {
        return allList.filter {
            it.countryName == country && it.sector == currentCategory
        }.map { it.productName }.toTypedArray()
    }

    private fun changedSector(sector: String) {
        delegate?.rightCountryContentReady(provideCountryProducts(rightCountry))
        delegate?.leftCountrContentReady(provideCountryProducts(leftCountry))
    }






    fun provideCountryList(): Array<String> {
        return allList.map { it.countryName }.toSet().toTypedArray()
    }

    fun provideSectorList(): Array<String> {
        return allList.map { it.sector }.toSet().toTypedArray()
    }

    fun changedElements(item: String, type: AlternativeRecyclerType) {

        when(type) {
            AlternativeRecyclerType.sector -> currentCategory = item
            AlternativeRecyclerType.leftCountry -> leftCountry = item
            AlternativeRecyclerType.rightCountry -> rightCountry = item
        }
    }


}



