package com.mtasdemir.brandapp.Detail

import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.Service.GoogleImageRequestDelegate
import com.mtasdemir.brandapp.Service.GoogleImageRequestService

interface DetailBrandViewModelDelegate {
    fun loadImage(url: String)
}

class DetailBrandViewModel: GoogleImageRequestDelegate {


    var delegate: DetailBrandViewModelDelegate? = null
    private val googleImageRequestService = GoogleImageRequestService()

    lateinit var queryItem: String

    private var lastSearchUrlList = mutableSetOf<String>()

    lateinit var brandModel: BrandModel

    fun viewDidLoad() {
        loadRequest()
    }

    private fun loadRequest() {
        googleImageRequestService.delegate = this
        googleImageRequestService.load(queryItem )
    }

    fun provideNewUrl(): String? {
        if (lastSearchUrlList.isEmpty()) {
            return null
        } else {
            val randomElement = lastSearchUrlList.random()
            lastSearchUrlList.remove(randomElement)
            return randomElement
        }
    }




    override fun imageUrlListReady(urlList: Array<String>) {
        lastSearchUrlList = urlList.toMutableSet()
        delegate?.loadImage(urlList.first())
        return
        val filteredList = urlList.filter {
            it.contains(queryItem.lowercase())
        }

        if (filteredList.isEmpty()) {
            if (!urlList.isEmpty()) {
                val randomElement = lastSearchUrlList.random()
                lastSearchUrlList.remove(randomElement)
                delegate?.loadImage(randomElement)
            }
        } else {
            val randomElement = filteredList.random()
            lastSearchUrlList.remove(randomElement)
            delegate?.loadImage(randomElement)
        }
    }


}