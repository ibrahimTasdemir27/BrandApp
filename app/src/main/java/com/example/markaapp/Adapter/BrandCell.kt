package com.example.markaapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.markaapp.BrandModel
import com.example.markaapp.R


class BrandCell(var items: Array<BrandModel>): RecyclerView.Adapter<BrandCell.BrandCellViewHolder>() {

    class BrandCellViewHolder(cell: View): RecyclerView.ViewHolder(cell) {
        val brandLabel: TextView = cell.findViewById(R.id.brandLabel)
        val brandDescriptionLabel: TextView = cell.findViewById(R.id.brandDescriptionLabel)
        val countryLabel: TextView = cell.findViewById(R.id.countryLabel)
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

        holder.brandLabel.text = model.name
        holder.brandDescriptionLabel.text = model.description
        holder.countryLabel.text = model.country


    }

}