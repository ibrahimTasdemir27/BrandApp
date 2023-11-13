package com.mtasdemir.brandapp.Home.Adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader
import com.mtasdemir.brandapp.Base.Base.View.BaseViewController
import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.Manager.SPrefencesManager
import com.mtasdemir.brandapp.R


interface BrandCellDelegate {
    fun tappedBrandItem(brandModel: BrandModel)
}

class BrandCell(var items: Array<BrandModel>): RecyclerView.Adapter<BrandCell.BrandCellViewHolder>() {

    var delegate: BrandCellDelegate? = null

    val bannedMarks = listOf(
        "Braun","Nescafe","Oreo","Netflix","Nike","Chevrolet","Cheetos","Ariel","Head & Shoulders",
        "KFC","McDonald's","Starbucks","Coca-Cola","Pepsi","Pepsi Co","Fanta","HP","Nestle","Nesquik Nestle",
        "Lays","Doritos","Omo","Puma","Sprite","Gilette","Maggi","Twix","Dove","Domestos","Papa Johns","Vakko",
        "Airbnb","Arbys","Johnson's","Danone","Dominos Pizza","Snickers","Knorr","Popeyes","Loreal","Milka",
        "Ben & Jerry","Cappy","Lipton","Schweppes","Fairy","Pantene","MacBook","Apple","Dell","Siemens",
        "Emporio Armani","Giorgio Armani"
    )
    private val redCountrys = SPrefencesManager.redCountrys
    private val greenCountrys = SPrefencesManager.greenCountrys

    class BrandCellViewHolder(cell: View): RecyclerView.ViewHolder(cell) {
        val brandLabel: TextView = cell.findViewById(R.id.brandLabel)
        val brandDescriptionLabel: TextView = cell.findViewById(R.id.brandDescriptionLabel)
        val countryLabel: TextView = cell.findViewById(R.id.countryLabel)
        val setSelectedButton: Button = cell.findViewById(R.id.brand_set_selected_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandCellViewHolder {
        val cell = LayoutInflater.from(parent.context).inflate(R.layout.brand_layout_cell, parent, false)

        return BrandCellViewHolder(cell)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: BrandCellViewHolder, position: Int) {
        val model = items[position]


        holder.brandLabel.text = model.productName
        holder.brandDescriptionLabel.text = model.sector
        holder.countryLabel.text = model.countryName

        if(bannedMarks.contains(model.productName)) {
            holder.brandLabel.setTextColor(ContextCompat.getColor(BaseViewController.appContext, R.color.red))
        } else {
            holder.brandLabel.setTextColor(ContextCompat.getColor(BaseViewController.appContext, R.color.black))
        }

        if(redCountrys.contains(model.countryName)) {
            holder.countryLabel.setTextColor(ContextCompat.getColor(BaseViewController.appContext, R.color.red))
        } else if (greenCountrys.contains(model.countryName)) {
            holder.countryLabel.setTextColor(ContextCompat.getColor(BaseViewController.appContext, R.color.green))
        } else {
            holder.countryLabel.setTextColor(Color.BLACK)
        }

        holder.setSelectedButton.setOnClickListener {
            delegate?.tappedBrandItem(items[position])
        }

    }

}