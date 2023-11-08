package com.example.markaapp.Home

import android.os.Parcelable
import com.example.markaapp.Base.Base.Protocols.Firebase.execute
import com.example.markaapp.Base.Base.Protocols.Firebase.executeOnlyArray
import com.example.markaapp.Base.Base.View.BaseViewModel
import com.example.markaapp.Service.FDBReadService
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.runBlocking


interface HomeViewModelDelegate {
    fun readyFilteredList(list: Array<BrandModel>)
    fun getBrandListFailure(err: String)
}


final class HomeViewModel: BaseViewModel() {

    private var allList = BrandModel.sampleBrands
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

    fun getBrands() {
        baseVMDelegate?.contentWillLoad()
        runBlocking {
            try {
                allList = ArrayList(FDBReadService.getBrandList().executeOnlyArray().toList())
                baseVMDelegate?.contentDidLoad()
                println("list success $allList.co")
            } catch (e: Error) {
                println("Error defined $e")
                delegate?.getBrandListFailure(e.toString())
            }
        }
    }




    fun filterTextIsChanged(text: String) {
        if (text == "") {
            delegate?.readyFilteredList(emptyList.toTypedArray())
            return
        }


        val filteredList = filterByText(text.lowercase())

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


@Parcelize
class BrandModel(
    val country: String = "",
    val name: String = "",
    val description: String = ""
): Parcelable {

    companion object {
        val sampleBrands = arrayListOf(
            BrandModel(name="Volkswagen", description="Otomotiv üretimi", country="Almanya"),
            BrandModel(name="BMW", description="Otomotiv üretimi", country="Almanya"),
            BrandModel(name="Mercedes-Benz", description="Otomotiv üretimi", country="Almanya"),
            BrandModel(name="Audi", description="Otomotiv üretimi", country="Almanya"),
            BrandModel(name="Porsche", description="Otomotiv üretimi", country="Almanya"),
            BrandModel(name="Adidas", description="Spor giyim ve ayakkabı üretimi", country="Almanya"),
            BrandModel(name="Siemens", description="Elektrik ve elektronik cihazlar üretimi", country="Almanya"),
            BrandModel(name="Bosch", description="Elektrikli aletler ve otomotiv parçaları üretimi", country="Almanya"),
            BrandModel(name="Bayer", description="İlaç ve kimyasal ürünler üretimi", country="Almanya"),
            BrandModel(name="Lufthansa", description="Havayolu taşımacılığı", country="Almanya"),
            BrandModel(name="SAP", description="İş yazılımı ve hizmet sağlayıcısı", country="Almanya"),
            BrandModel(name="Aldi", description="Süpermarket zinciri", country="Almanya"),
            BrandModel(name="ThyssenKrupp", description="Çelik ve sanayi malzemeleri üretimi", country="Almanya"),
            BrandModel(name="Allianz", description="Sigorta şirketi", country="Almanya"),
            BrandModel(name="DHL", description="Lojistik ve kargo hizmetleri sağlayıcısı", country="Almanya"),
            BrandModel(name="BASF", description="Kimyasal ürünler üretimi", country="Almanya"),
            BrandModel(name="Beiersdorf", description="Kişisel bakım ürünleri üretimi", country="Almanya"),
            BrandModel(name="Daimler", description="Otomotiv üretimi", country="Almanya"),
            BrandModel(name="Fresenius", description="Sağlık hizmetleri ve ilaç üretimi", country="Almanya"),
            BrandModel(name="Infineon Technologies", description="Yarı iletken üretimi", country="Almanya"),
            BrandModel(name="Continental AG", description="Otomotiv lastikleri ve otomotiv sistemleri üretimi", country="Almanya"),
            BrandModel(name="Miele", description="Beyaz eşya ve ev aletleri üretimi", country="Almanya"),
            BrandModel(name="Puma", description="Spor giyim ve ayakkabı üretimi", country="Almanya"),
            BrandModel(name="TUI", description="Turizm şirketi", country="Almanya"),
            BrandModel(name="Deutsche Post", description="Posta ve lojistik hizmetleri sağlayıcısı", country="Almanya"),
            BrandModel(name="Commerzbank", description="Banka", country="Almanya"),
            BrandModel(name="Knorr-Bremse", description="Otomotiv fren sistemleri üretimi", country="Almanya"),
            BrandModel(name="Messe Frankfurt", description="Fuar ve etkinlik organizasyonu sağlayıcısı", country="Almanya"),
            BrandModel(name="Metro Cash & Carry", description="Toptan satış marketleri", country="Almanya"),
            BrandModel(name="Tchibo", description="Kahve ve perakende şirketi", country="Almanya"),
            BrandModel(name="Freenet", description="Telekomünikasyon hizmetleri sağlayıcısı", country="Almanya"),
            BrandModel(name="Innogy", description="Enerji şirketi", country="Almanya"),
            BrandModel(name="Deutsche Bank", description="Banka", country="Almanya"),
            BrandModel(name="RWE", description="Enerji şirketi", country="Almanya"),
            BrandModel(name="Henkel", description="Tüketici ürünleri ve kimyasallar üretimi", country="Almanya"),
            BrandModel(name="Merck KGaA", description="İlaç ve kimyasal ürünler üretimi", country="Almanya"),
            BrandModel(name="Fraport", description="Havalimanı işletmecisi", country="Almanya"),
            BrandModel(name="Hugo Boss", description="Moda ve lüks giyim üretimi", country="Almanya"),
            BrandModel(name="Kärcher", description="Temizlik ekipmanları üretimi", country="Almanya"),
            BrandModel(name="Lidl", description="Süpermarket zinciri", country="Almanya"),
            BrandModel(name="Osram", description="Aydınlatma ve optoelektronik ürünler üretimi", country="Almanya"),
            BrandModel(name="TUI Group", description="Turizm şirketi", country="Almanya"),
            BrandModel(name="Wirecard", description="Ödeme işlemleri ve finansal hizmetler", country="Almanya"),
            BrandModel(name="Vonovia", description="Konut ve gayrimenkul yönetimi", country="Almanya"),
            BrandModel(name="Fraport", description="Havalimanı işletmecisi", country="Almanya"),
            BrandModel(name="Hugo Boss", description="Moda ve lüks giyim üretimi", country="Almanya"),
            BrandModel(name="Kärcher", description="Temizlik ekipmanları üretimi", country="Almanya"),
            BrandModel(name="Lidl", description="Süpermarket zinciri", country="Almanya"),
            BrandModel(name="Osram", description="Aydınlatma ve optoelektronik ürünler üretimi", country="Almanya"),
            BrandModel(name="TUI Group", description="Turizm şirketi", country="Almanya"),
            BrandModel(name="Wirecard", description="Ödeme işlemleri ve finansal hizmetler", country="Almanya"),
            BrandModel(name="Vonovia", description="Konut ve gayrimenkul yönetimi", country="Almanya"),
            BrandModel(name="Fraport", description="Havalimanı işletmecisi", country="Almanya"),
            BrandModel(name="Hugo Boss", description="Moda ve lüks giyim üretimi", country="Almanya"),
            BrandModel(name="Kärcher", description="Temizlik ekipmanları üretimi", country="Almanya"),
            BrandModel(name="Lidl", description="Süpermarket zinciri", country="Almanya"),
            BrandModel(name="Osram", description="Aydınlatma ve optoelektronik ürünler üretimi", country="Almanya"),
            BrandModel(name="TUI Group", description="Turizm şirketi", country="Almanya"),
            BrandModel(name="Wirecard", description="Ödeme işlemleri ve finansal hizmetler", country="Almanya"),
            BrandModel(name="Vonovia", description="Konut ve gayrimenkul yönetimi", country="Almanya"),




        )
    }

}
