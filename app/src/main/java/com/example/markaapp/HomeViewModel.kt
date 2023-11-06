package com.example.markaapp


interface HomeViewModelDelegate {
    fun readyFilteredList(list: Array<BrandModel>)
}


final class HomeViewModel {

    private val allList = BrandModel.sampleBrands
    private val emptyList = ArrayList<BrandModel>()


    var isFilterToBrand: Boolean = true
        set(value) {
            filterTextIsChanged(lastSearchText)
            field = value
        }


    var delegate: HomeViewModelDelegate? = null

    private var lastSearchText: String = ""




    fun filterTextIsChanged(text: String) {
        if (text == "") {
            delegate?.readyFilteredList(emptyList.toTypedArray())
            return
        }


        val filteredList = filterByText(text.lowercase())
        println("Prepared List by Filter Text: $text \n $filteredList")

        delegate?.readyFilteredList(filteredList)
        lastSearchText = text
    }

    private fun filterByText(text: String): Array<BrandModel> {
        if (isFilterToBrand) {
            return filterByBrand(text)
        } else {
            return filterByCountry(text)
        }
    }

    private fun filterByBrand(text: String): Array<BrandModel> {
        val filteredList = allList.filter {
            it.name.lowercase().startsWith(text)
        }.sortedBy { it.name }



        return filteredList.toTypedArray()
    }

    private fun filterByCountry(text: String): Array<BrandModel> {
        val filteredList = allList.filter {
            it.country.lowercase().startsWith(text)
        }.sortedBy { it.country }

        return filteredList.toTypedArray()
    }

}


class BrandModel(
    val id: String,
    val country: String,
    val name: String,
    val description: String
) {

    companion object {
        val sampleBrands = arrayListOf(
            BrandModel("xx", "Türkiye", "Togg", "Bir araba markası"),
            BrandModel("xx", "İsrail", "Burn", "Bir içecek markası"),
            BrandModel("xx", "İsrail", "Coca Cola", "Bir içecek markası"),
            BrandModel("xx", "Amerika", "Ford", "Bir araba markası"),
            BrandModel("xx", "Almanya", "Bosch", "Bir elektronik markası"),
            BrandModel("xx", "İngiltere", "Dyson", "Dyson, katı hal pil hücreleri, yüksek hızlı elektrik motorları, görüntü sistemleri, makine öğrenimi teknolojileri ve yapay zekaya odaklanan küresel ekiplerle yeni teknolojiler geliştirmek için iddialı planlar gerçekleştiriyor. Dyson'ın şirket içi robotik ekibi, Birleşik Krallık'taki en büyük ekiplerden biridir."),
            BrandModel("xx", "Almanya", "BMW", "Bir araba markası"),
        )
    }

}
