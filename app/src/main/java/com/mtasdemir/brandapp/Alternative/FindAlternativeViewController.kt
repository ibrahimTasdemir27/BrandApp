package com.mtasdemir.brandapp.Alternative

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mtasdemir.brandapp.Alternative.Adapter.AlternativeBrandCell
import com.mtasdemir.brandapp.Alternative.Adapter.AlternativeCell
import com.mtasdemir.brandapp.Alternative.Adapter.CustomDropDownAdapter
import com.mtasdemir.brandapp.Base.Base.BaseError
import com.mtasdemir.brandapp.Base.Base.View.BaseViewController
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.databinding.FindAlternativeLayoutBinding

class FindAlternativeViewController:
    BaseViewController(),
    FindAlternativeViewModelDelegate {


    private val viewModel = FindAlternativeViewModel()

    override fun provideViewModel(): BaseViewModel? {
        return viewModel
    }

    private val binding: FindAlternativeLayoutBinding by lazy {
        FindAlternativeLayoutBinding.inflate(layoutInflater)
    }



    lateinit var selectSectorSpinner: Spinner
    lateinit var selectLeftCountrySpinner: Spinner
    lateinit var selectRightCountrySpinner: Spinner


    lateinit var brandLeftCountryRecyclerView: RecyclerView
    lateinit var brandRightCountryRecyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.delegate = this
        createSuccessModifieElements()
        bindItem()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }


    private fun bindItem() {
        selectSectorSpinner = binding.selectSectorSpinner
        selectLeftCountrySpinner = binding.selectLeftCountrySpinner
        selectRightCountrySpinner = binding.selectRightCountrySpinner

        brandLeftCountryRecyclerView = binding.brandsLeftRecyclerView
        brandRightCountryRecyclerView = binding.brandsRightRecyclerView


        /** For Brand Recycler Adapater Binding **/
        brandLeftCountryRecyclerView.adapter = AlternativeCell(emptyArray(), true)
        brandLeftCountryRecyclerView.layoutManager = LinearLayoutManager(this)

        brandRightCountryRecyclerView.adapter = AlternativeCell(emptyArray(), false)
        brandRightCountryRecyclerView.layoutManager = LinearLayoutManager(this)


        /** Spinner Binding Adapter **/
        val sectorList = viewModel.provideSectorList()
        val countryList = viewModel.provideCountryList()

        val israelIndex = countryList.indexOfFirst { it == "İsrail" }
        val turkeyIdex = countryList.indexOfFirst { it == "Türkiye" }

        val sectorAdapter = CustomDropDownAdapter(this, sectorList, 0)
        selectSectorSpinner.adapter = sectorAdapter


        val leftCountryAdapter = CustomDropDownAdapter(this, countryList, israelIndex)
        selectLeftCountrySpinner.adapter = leftCountryAdapter

        val rightCountryAdapter = CustomDropDownAdapter(this, countryList, turkeyIdex)
        selectRightCountrySpinner.adapter = rightCountryAdapter





        selectLeftCountrySpinner.setSelection(israelIndex)
        selectRightCountrySpinner.setSelection(turkeyIdex)

        selectSectorSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                val sectorText = sectorList[position]
                val adapter = selectSectorSpinner.adapter as CustomDropDownAdapter

                adapter.selectedPosition = position
                adapter.notifyDataSetChanged()
                viewModel.currentCategory = sectorText
//                Toast.makeText(this@FindAlternativeViewController,
//                    sectorText, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        selectLeftCountrySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                val countryText = countryList[position]
                val adapter = selectSectorSpinner.adapter as CustomDropDownAdapter
                adapter.selectedPosition = position
                adapter.notifyDataSetChanged()

                viewModel.leftCountry = countryText
//                Toast.makeText(this@FindAlternativeViewController,
//                    countryText, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        selectRightCountrySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                val countryText = countryList[position]
                val adapter = selectSectorSpinner.adapter as CustomDropDownAdapter
                adapter.selectedPosition = position
                adapter.notifyDataSetChanged()

                viewModel.rightCountry = countryText
//                Toast.makeText(this@FindAlternativeViewController,
//                    countryText, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    override fun createSuccessModifieElements() {
        super.createSuccessModifieElements()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.allList = intent.getParcelableArrayExtra("allBrands", BrandModel::class.java)!!
            println("Tüm marklar ${viewModel.allList.count()}")
        } else {
            viewModel.allList = intent.getParcelableArrayExtra("allBrands") as Array<BrandModel>
            println("Tüm marklar2 ${viewModel.allList}")
        }
    }




    /**
    Button Actions
     */
    @SuppressLint("NotifyDataSetChanged")
//    private fun tappedSectorButton() {
//        val adapter = provideAdapterAlternative(AlternativeRecyclerType.sector)
//        adapter.typeList = viewModel.provideSectorList()
//        adapter.notifyDataSetChanged()
//
//
//    }

    //@SuppressLint("NotifyDataSetChanged")
    private fun tappedLeftCountryButton() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun tappedRightCountryButton() {

    }

    /**
     * ViewModel Delegate
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun leftCountrContentReady(list: Array<String>) {
        val adapter = brandLeftCountryRecyclerView.adapter as AlternativeCell
        adapter.typeList = list
        adapter.notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun rightCountryContentReady(list: Array<String>) {
        val adapter = brandRightCountryRecyclerView.adapter as AlternativeCell
        adapter.typeList = list
        adapter.notifyDataSetChanged()

    }

    override fun getContentFailure(failure: BaseError) {
        println("Hata bulundu $failure")
    }

    companion object {
        fun create(allBrands: Array<BrandModel>): Intent {
            val intent = Intent(appContext, FindAlternativeViewController::class.java)
            intent.putExtra("allBrands", allBrands)
            return intent
        }
    }



}