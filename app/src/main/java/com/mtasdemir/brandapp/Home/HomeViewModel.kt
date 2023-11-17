package com.mtasdemir.brandapp.Home

import android.os.Parcelable
import com.mtasdemir.brandapp.Base.Base.BaseError
import com.mtasdemir.brandapp.Base.Base.Helper.Helper
import com.mtasdemir.brandapp.Base.Base.Protocols.Firebase.executeOnlyArray
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.Service.BrandNextModel
import com.mtasdemir.brandapp.Service.FDBReadService
import com.mtasdemir.brandapp.Service.FDBWriteService
import com.mtasdemir.brandapp.Service.GoogleImageRequestDelegate
import com.mtasdemir.brandapp.Service.GoogleImageRequestService
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout


interface HomeViewModelDelegate {
    fun readyFilteredList(list: Array<BrandModel>)
    fun getBrandListFailure(err: String)
}


final class HomeViewModel: BaseViewModel(), GoogleImageRequestDelegate {

    var allList = ArrayList<BrandModel>()
    private val emptyList = ArrayList<BrandModel>()


    var isFilterToBrand: Boolean = true
        set(value) {
            filterTextIsChanged(lastSearchText)
            field = value
        }


    var delegate: HomeViewModelDelegate? = null

    private var lastSearchText: String = ""

    override fun onCreate() {
        super.onCreate()
        getBrands()
    }


    override fun imageUrlListReady(urlList: Array<String>) {
        println("ReadyURL:: ${urlList.first()}")

    }

    var counter = 0

    override fun imgBrandNextModel(brandModel: BrandModel, url: String) {
   /*     val model = BrandNextModel(countryName = brandModel.countryName, productName = brandModel.productName, sector = brandModel.sector, brandImageUrl = url)
        runBlocking {
            FDBWriteService.setNewBrandItem(model, counter).update()
            println("Saved URL: $url")
            counter++
        }
        
    */

    }

    fun getBrands() {
        baseVMDelegate?.contentWillLoad()
        runBlocking {

                try {
                    allList = ArrayList(FDBReadService.getBrandList().executeOnlyArray().toList())
                    baseVMDelegate?.contentDidLoad()
                } catch (e: BaseError) {
                    baseVMDelegate?.contentDidLoad()
                    delegate?.getBrandListFailure(e.toString())
                }

        }
    }




    fun filterTextIsChanged(text: String) {
        if (text == "") {
            delegate?.readyFilteredList(emptyList.toTypedArray())
            return
        }


        val filteredList = filterByText(Helper.lowercase(text))

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
            Helper.lowercase(it.productName).startsWith(text)
        }.sortedBy { it.productName }



        return filteredList.toTypedArray()
    }

    private fun filterByCountry(text: String): Array<BrandModel> {
        val filteredList = allList.filter {
            Helper.lowercase(it.countryName).startsWith(text)
        }.sortedBy { it.countryName }


        return filteredList.toTypedArray()
    }

}


@Parcelize
class BrandModel(
    val countryName: String = "",
    val productName: String = "",
    val sector: String = ""
): Parcelable {

    companion object {
        val sampleBrands = arrayListOf(
            BrandModel(productName="Volkswagen", sector="Otomotiv üretimi", countryName="Almanya"),
            BrandModel(productName="BMW", sector="Otomotiv üretimi", countryName="Almanya"),
            BrandModel(productName="Mercedes-Benz", sector="Otomotiv üretimi", countryName="Almanya"),
            BrandModel(productName="Audi", sector="Otomotiv üretimi", countryName="Almanya"),
            BrandModel(productName="Porsche", sector="Otomotiv üretimi", countryName="Almanya"),
            BrandModel(productName="Adidas", sector="Spor giyim ve ayakkabı üretimi", countryName="Almanya"),
            BrandModel(productName="Siemens", sector="Elektrik ve elektronik cihazlar üretimi", countryName="Almanya"),
            BrandModel(productName="Bosch", sector="Elektrikli aletler ve otomotiv parçaları üretimi", countryName="Almanya"),
            BrandModel(productName="Bayer", sector="İlaç ve kimyasal ürünler üretimi", countryName="Almanya"),
            BrandModel(productName="Lufthansa", sector="Havayolu taşımacılığı", countryName="Almanya"),
            BrandModel(productName="SAP", sector="İş yazılımı ve hizmet sağlayıcısı", countryName="Almanya"),
            BrandModel(productName="Aldi", sector="Süpermarket zinciri", countryName="Almanya"),
            BrandModel(productName="ThyssenKrupp", sector="Çelik ve sanayi malzemeleri üretimi", countryName="Almanya"),
            BrandModel(productName="Allianz", sector="Sigorta şirketi", countryName="Almanya"),
            BrandModel(productName="DHL", sector="Lojistik ve kargo hizmetleri sağlayıcısı", countryName="Almanya"),
            BrandModel(productName="BASF", sector="Kimyasal ürünler üretimi", countryName="Almanya"),
            BrandModel(productName="Beiersdorf", sector="Kişisel bakım ürünleri üretimi", countryName="Almanya"),
            BrandModel(productName="Daimler", sector="Otomotiv üretimi", countryName="Almanya"),
            BrandModel(productName="Fresenius", sector="Sağlık hizmetleri ve ilaç üretimi", countryName="Almanya"),
            BrandModel(productName="Infineon Technologies", sector="Yarı iletken üretimi", countryName="Almanya"),
            BrandModel(productName="Continental AG", sector="Otomotiv lastikleri ve otomotiv sistemleri üretimi", countryName="Almanya"),
            BrandModel(productName="Miele", sector="Beyaz eşya ve ev aletleri üretimi", countryName="Almanya"),
            BrandModel(productName="Puma", sector="Spor giyim ve ayakkabı üretimi", countryName="Almanya"),
            BrandModel(productName="TUI", sector="Turizm şirketi", countryName="Almanya"),
            BrandModel(productName="Deutsche Post", sector="Posta ve lojistik hizmetleri sağlayıcısı", countryName="Almanya"),
            BrandModel(productName="Commerzbank", sector="Banka", countryName="Almanya"),
            BrandModel(productName="Knorr-Bremse", sector="Otomotiv fren sistemleri üretimi", countryName="Almanya"),
            BrandModel(productName="Messe Frankfurt", sector="Fuar ve etkinlik organizasyonu sağlayıcısı", countryName="Almanya"),
            BrandModel(productName="Metro Cash & Carry", sector="Toptan satış marketleri", countryName="Almanya"),
            BrandModel(productName="Tchibo", sector="Kahve ve perakende şirketi", countryName="Almanya"),
            BrandModel(productName="Freenet", sector="Telekomünikasyon hizmetleri sağlayıcısı", countryName="Almanya"),
            BrandModel(productName="Innogy", sector="Enerji şirketi", countryName="Almanya"),
            BrandModel(productName="Deutsche Bank", sector="Banka", countryName="Almanya"),
            BrandModel(productName="RWE", sector="Enerji şirketi", countryName="Almanya"),
            BrandModel(productName="Henkel", sector="Tüketici ürünleri ve kimyasallar üretimi", countryName="Almanya"),
            BrandModel(productName="Merck KGaA", sector="İlaç ve kimyasal ürünler üretimi", countryName="Almanya"),
            BrandModel(productName="Fraport", sector="Havalimanı işletmecisi", countryName="Almanya"),
            BrandModel(productName="Hugo Boss", sector="Moda ve lüks giyim üretimi", countryName="Almanya"),
            BrandModel(productName="Kärcher", sector="Temizlik ekipmanları üretimi", countryName="Almanya"),
            BrandModel(productName="Lidl", sector="Süpermarket zinciri", countryName="Almanya"),
            BrandModel(productName="Osram", sector="Aydınlatma ve optoelektronik ürünler üretimi", countryName="Almanya"),
            BrandModel(productName="TUI Group", sector="Turizm şirketi", countryName="Almanya"),
            BrandModel(productName="Wirecard", sector="Ödeme işlemleri ve finansal hizmetler", countryName="Almanya"),
            BrandModel(productName="Vonovia", sector="Konut ve gayrimenkul yönetimi", countryName="Almanya"),
            BrandModel(productName="Fraport", sector="Havalimanı işletmecisi", countryName="Almanya"),
            BrandModel(productName="Hugo Boss", sector="Moda ve lüks giyim üretimi", countryName="Almanya"),
            BrandModel(productName="Kärcher", sector="Temizlik ekipmanları üretimi", countryName="Almanya"),
            BrandModel(productName="Lidl", sector="Süpermarket zinciri", countryName="Almanya"),
            BrandModel(productName="Osram", sector="Aydınlatma ve optoelektronik ürünler üretimi", countryName="Almanya"),
            BrandModel(productName="TUI Group", sector="Turizm şirketi", countryName="Almanya"),
            BrandModel(productName="Wirecard", sector="Ödeme işlemleri ve finansal hizmetler", countryName="Almanya"),
            BrandModel(productName="Vonovia", sector="Konut ve gayrimenkul yönetimi", countryName="Almanya"),
            BrandModel(productName="Fraport", sector="Havalimanı işletmecisi", countryName="Almanya"),
            BrandModel(productName="Hugo Boss", sector="Moda ve lüks giyim üretimi", countryName="Almanya"),
            BrandModel(productName="Kärcher", sector="Temizlik ekipmanları üretimi", countryName="Almanya"),
            BrandModel(productName="Lidl", sector="Süpermarket zinciri", countryName="Almanya"),
            BrandModel(productName="Osram", sector="Aydınlatma ve optoelektronik ürünler üretimi", countryName="Almanya"),
            BrandModel(productName="TUI Group", sector="Turizm şirketi", countryName="Almanya"),
            BrandModel(productName="Wirecard", sector="Ödeme işlemleri ve finansal hizmetler", countryName="Almanya"),
            BrandModel(productName="Vonovia", sector="Konut ve gayrimenkul yönetimi", countryName="Almanya"),




        )
    }

}
