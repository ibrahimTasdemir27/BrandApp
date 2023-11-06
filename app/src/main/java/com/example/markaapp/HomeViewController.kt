package com.example.markaapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.markaapp.Adapter.BrandCell
import com.example.markaapp.databinding.ActivityMainBinding

class HomeViewController : AppCompatActivity(), HomeViewModelDelegate {


    private val viewModel = HomeViewModel()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var searchTextField: TextView
    lateinit var filterButton: Button
    lateinit var filterButtonTextView: TextView
    lateinit var recyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.delegate = this
        modifieElements()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }


    private fun modifieElements() {
        searchTextField = binding.searchTextField
        filterButton = binding.filterButton
        filterButtonTextView = binding.filterButtonTextView
        recyclerView = binding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)
        val emptyArray = ArrayList<BrandModel>()
        val adapter = BrandCell(emptyArray.toTypedArray())
        recyclerView.adapter = adapter


        searchTextField.addTextChangedListener {
            textIsChanged()
        }

        binding.filterButton.setOnClickListener {
            filterButtonTapped()
        }
    }


    private fun textIsChanged() {
        viewModel.filterTextIsChanged(searchTextField.text.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun filterButtonTapped() {
        val isFilterToBrand = !viewModel.isFilterToBrand

        if (isFilterToBrand) {
            searchTextField.hint = "Marka Adını Yazınız..."
            filterButtonTextView.text = "Ülkeye göre"
        } else {
            searchTextField.hint = "Ülke Adını Yazınız..."
            filterButtonTextView.text = "Markaya göre"
        }

        viewModel.isFilterToBrand = isFilterToBrand
    }


    /** HomeViewModel Delegate **/
    @SuppressLint("NotifyDataSetChanged")
    override fun readyFilteredList(list: Array<BrandModel>) {
        println("list is change")
        val adapter = recyclerView.adapter as BrandCell
        adapter.items = list
        adapter.notifyDataSetChanged()

    }


}