package com.mtasdemir.brandapp.Alternative

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mtasdemir.brandapp.Alternative.Adapter.AlternativeBrandCell
import com.mtasdemir.brandapp.Alternative.Adapter.AlternativeCell
import com.mtasdemir.brandapp.Alternative.Adapter.AlternativeCellDelegate
import com.mtasdemir.brandapp.Alternative.Adapter.AlternativeRecyclerType
import com.mtasdemir.brandapp.Alternative.Adapter.CustomDropDownAdapter
import com.mtasdemir.brandapp.Base.Base.BaseError
import com.mtasdemir.brandapp.Base.Base.View.BaseViewController
import com.mtasdemir.brandapp.Base.Base.View.BaseViewModel
import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.databinding.FindAlternativeLayoutBinding

class FindAlternativeViewController:
    BaseViewController(),
    FindAlternativeViewModelDelegate,
    AlternativeCellDelegate {


    private val viewModel = FindAlternativeViewModel()

    override fun provideViewModel(): BaseViewModel? {
        return viewModel
    }

    private val binding: FindAlternativeLayoutBinding by lazy {
        FindAlternativeLayoutBinding.inflate(layoutInflater)
    }


    lateinit var selectSectorLinearLayout: LinearLayout
    lateinit var selectSectorTextView: TextView
    lateinit var selectLeftCountryTextView: TextView
    lateinit var selectRightCountryTextView: TextView

    lateinit var selectSectorRecyclerView: RecyclerView
    lateinit var selectSectorSpinner: Spinner
    lateinit var selectCountryLeftRecyclerView: RecyclerView
    lateinit var selectCountryRightRecyclerView: RecyclerView

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
        //selectSectorLinearLayout = binding.selectSectorLinearLayout
        //selectSectorTextView = binding.selectSectorTextView
        selectLeftCountryTextView = binding.selectCountryLeftTextview
        selectRightCountryTextView = binding.selectCountryRightTextview

        //selectSectorRecyclerView = binding.selectSectorRecyclerview


        selectCountryLeftRecyclerView = binding.selectLeftCountryRecyclerview
        selectCountryRightRecyclerView = binding.selectRightCountryRecyclerview

        brandLeftCountryRecyclerView = binding.brandsLeftRecyclerView
        brandRightCountryRecyclerView = binding.brandsRightRecyclerView



        val emptyArray = ArrayList<String>().toTypedArray()

//        val sectorAdapter = AlternativeCell(emptyArray, AlternativeRecyclerType.sector)
//        sectorAdapter.delegate = this
        val list = viewModel.provideSectorList() //resources.getStringArray(R.array.emailAddressTypes)
        selectSectorSpinner = binding.selectSectorSpinner
        val adapter = CustomDropDownAdapter(this, list)
        selectSectorSpinner.adapter = adapter
        selectSectorSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                Toast.makeText(this@FindAlternativeViewController,
                    list.get(position), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        //selectSectorRecyclerView.layoutManager = LinearLayoutManager(this)
        //selectSectorRecyclerView.adapter = sectorAdapter


        val leftCountryAdapter = AlternativeCell(emptyArray, AlternativeRecyclerType.leftCountry)
        leftCountryAdapter.delegate = this
        selectCountryLeftRecyclerView.layoutManager = LinearLayoutManager(this)
        selectCountryLeftRecyclerView.adapter = leftCountryAdapter


        val rightCountryAdapter = AlternativeCell(emptyArray, AlternativeRecyclerType.rightCountry)
        rightCountryAdapter.delegate = this
        selectCountryRightRecyclerView.layoutManager = LinearLayoutManager(this)
        selectCountryRightRecyclerView.adapter = rightCountryAdapter

        brandLeftCountryRecyclerView.layoutManager = LinearLayoutManager(this)
        brandLeftCountryRecyclerView.adapter = AlternativeBrandCell(emptyArray, true)

        brandRightCountryRecyclerView.layoutManager = LinearLayoutManager(this)
        brandRightCountryRecyclerView.adapter = AlternativeBrandCell(emptyArray, false)



//        binding.selectSectorButton.setOnClickListener {
//            tappedSectorButton()
//        }

//        binding.selectLeftCountryButton.setOnClickListener {
//            tappedLeftCountryButton()
//        }

//        binding.selectRightCountryButton.setOnClickListener {
//            tappedRightCountryButton()
//        }
        // --- MT
//        binding.selectSectorTextView.setOnClickListener {
//            tappedSectorButton()
//        }

        binding.selectCountryRightTextview.setOnClickListener {
            tappedRightCountryButton()
        }
        binding.selectCountryLeftTextview.setOnClickListener {
            tappedLeftCountryButton()
        }
        // --- MT


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
        val adapter = provideAdapterAlternative(AlternativeRecyclerType.leftCountry)

        adapter.typeList = viewModel.provideCountryList()

        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun tappedRightCountryButton() {
        val adapter = provideAdapterAlternative(AlternativeRecyclerType.rightCountry)
        adapter.typeList = viewModel.provideCountryList()
        adapter.notifyDataSetChanged()
    }












    /**
     * Alternative Cell Delegate
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun selectedAlternativeItem(item: String, type: AlternativeRecyclerType) {
        val adapter = provideAdapterAlternative(type)
        adapter.typeList = ArrayList<String>().toTypedArray()
        viewModel.changedElements(item, type)
        adapter.notifyDataSetChanged()
    }
















    /**
     * ViewModel Delegate
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun leftCountrContentReady(list: Array<String>) {
        val adapter = brandLeftCountryRecyclerView.adapter as AlternativeBrandCell
        selectLeftCountryTextView.text = viewModel.leftCountry
        adapter.brandNameList = list
        adapter.notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun rightCountryContentReady(list: Array<String>) {
        val adapter = brandRightCountryRecyclerView.adapter as AlternativeBrandCell
        selectRightCountryTextView.text = viewModel.rightCountry
        adapter.brandNameList = list
        adapter.notifyDataSetChanged()

    }


    override fun changedSector(sector: String) {
        selectSectorTextView.text = sector.lowercase().replaceFirstChar { it.uppercase() }
    }

    override fun getContentFailure(failure: BaseError) {
        println("Hata bulundu $failure")
    }












    private fun provideAdapterAlternative(recyclerType: AlternativeRecyclerType): AlternativeCell {
        return when(recyclerType) {
            //AlternativeRecyclerType.sector -> selectSectorRecyclerView.adapter as AlternativeCell
            AlternativeRecyclerType.leftCountry -> selectCountryLeftRecyclerView.adapter as AlternativeCell
            AlternativeRecyclerType.rightCountry -> selectCountryRightRecyclerView.adapter as AlternativeCell
        }
    }




    companion object {
        fun create(allBrands: Array<BrandModel>): Intent {
            val intent = Intent(appContext, FindAlternativeViewController::class.java)
            intent.putExtra("allBrands", allBrands)
            return intent
        }
    }



}