package com.mtasdemir.brandapp.CountryPalette.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mtasdemir.brandapp.CountryPalette.PaletteCountryCellModel
import com.mtasdemir.brandapp.R

interface CountryPaletteCellDelegate {
    fun didSelectPaletteItem(selectedCountry: String, isLeft: Boolean)
}

class CountryPaletteCell(var countryList: Array<PaletteCountryCellModel>, val isLeft: Boolean): RecyclerView.Adapter<CountryPaletteCell.CountryPaletteViewHolder>() {

    var delegate: CountryPaletteCellDelegate? = null

    class CountryPaletteViewHolder(cell: View): RecyclerView.ViewHolder(cell) {
        val countryTextView: TextView = cell.findViewById(R.id.palette_country_description_label)
        val selectedButton: Button = cell.findViewById(R.id.palette_selected_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryPaletteViewHolder {
        val holder = LayoutInflater.from(parent.context).inflate(R.layout.palette_country_cell, parent, false)
        return CountryPaletteViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return countryList.count()
    }

    override fun onBindViewHolder(holder: CountryPaletteViewHolder, position: Int) {
        val country = countryList[position].country
        holder.countryTextView.text = country

        if (isLeft) {
            holder.countryTextView.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        } else {
            holder.countryTextView.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        }

        if (isLeft && countryList[position].isSelected) {
            holder.countryTextView.setTextColor(Color.RED)
        } else if (!isLeft && countryList[position].isSelected) {
            holder.countryTextView.setTextColor(Color.GREEN)
        } else {
            holder.countryTextView.setTextColor(Color.BLACK)
        }

        holder.selectedButton.setOnClickListener {
            delegate?.didSelectPaletteItem(country, isLeft)
        }

    }

}