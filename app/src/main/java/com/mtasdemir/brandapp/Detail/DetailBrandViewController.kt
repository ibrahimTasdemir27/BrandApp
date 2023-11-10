package com.mtasdemir.brandapp.Detail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.databinding.DetailBrandLayoutBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailBrandViewController: AppCompatActivity(), DetailBrandViewModelDelegate {

    private val binding: DetailBrandLayoutBinding by lazy {
        DetailBrandLayoutBinding.inflate(layoutInflater)
    }


    lateinit var image: ImageView
    lateinit var countryTextView: TextView
    lateinit var brandNameTextView: TextView
    lateinit var brandDescriptionTextView: TextView
    lateinit var backButton: Button


    private var viewModel = DetailBrandViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.delegate = this
        modifieElements()
        bindItem()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.viewDidLoad()
        initUI()

    }


    private fun bindItem() {
        backButton = binding.detaiBackButton
        image = binding.detailImage
        countryTextView = binding.detailBrandCountryTextview
        brandNameTextView = binding.detailBrandBrandNameTextview
        brandDescriptionTextView = binding.detailBrandDescriptionTextview

        backButton.setOnClickListener {
            tappedBackButton()
        }
    }

    private fun initUI() {
        countryTextView.text = viewModel.brandModel.countryName
        brandNameTextView.text = viewModel.brandModel.productName
        brandDescriptionTextView.text = viewModel.brandModel.sector
    }

    private fun modifieElements() {
        viewModel.queryItem = intent.getStringExtra("queryText").toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.brandModel = intent.getParcelableExtra("brandModel", BrandModel::class.java)!!
        } else {
            viewModel.brandModel = intent.getParcelableExtra("brandModel")!!
        }

    }


    private fun tappedBackButton() {
        finish()
    }

    override fun loadImage(url: String) {
        println("Loading images $url")
        MainScope().launch {
            withContext(Dispatchers.Main) {
                Picasso.with(this@DetailBrandViewController).load(url).placeholder(androidx.constraintlayout.widget.R.drawable.abc_ic_clear_material).into(image)
            }
        }
    }


    companion object {
        fun create(context: Context, queryText: String, brandModel: BrandModel): Intent {
            val intent = Intent(context, DetailBrandViewController::class.java)
            intent.putExtra("queryText", queryText)
            intent.putExtra("brandModel", brandModel)

            return intent
        }
    }

}


