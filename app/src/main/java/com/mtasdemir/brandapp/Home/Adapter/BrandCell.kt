package com.mtasdemir.brandapp.Home.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mtasdemir.brandapp.Home.BrandModel
import com.mtasdemir.brandapp.R


interface BrandCellDelegate {
    fun tappedBrandItem(brandModel: BrandModel)
}

class BrandCell(var items: Array<BrandModel>): RecyclerView.Adapter<BrandCell.BrandCellViewHolder>() {

    var delegate: BrandCellDelegate? = null
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

        holder.setSelectedButton.setOnClickListener {
            delegate?.tappedBrandItem(items[position])
        }

    }

}