package com.mtasdemir.brandapp.Alternative.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mtasdemir.brandapp.R


interface AlternativeCellDelegate {
    fun selectedAlternativeItem(item: String, type: AlternativeRecyclerType)
}

class AlternativeCell(var typeList: Array<String>, val recylerType: AlternativeRecyclerType): RecyclerView.Adapter<AlternativeCell.AlternativeCellViewHolder>() {


    var delegate: AlternativeCellDelegate? = null

    class AlternativeCellViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionLabel)
        val selectedButton: Button = view.findViewById(R.id.alternative_selectedButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlternativeCellViewHolder {
        val holder = LayoutInflater.from(parent.context).inflate(R.layout.aletnative_select_element_cell, parent, false)
        return AlternativeCellViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return typeList.count()
    }

    override fun onBindViewHolder(holder: AlternativeCellViewHolder, position: Int) {
        holder.descriptionTextView.text = typeList[position]
        when(recylerType) {
            AlternativeRecyclerType.sector ->
            holder.descriptionTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            AlternativeRecyclerType.leftCountry -> View.TEXT_ALIGNMENT_TEXT_START
            AlternativeRecyclerType.rightCountry -> View.TEXT_ALIGNMENT_TEXT_END
        }

        holder.selectedButton.setOnClickListener {
            println("Delegate is $delegate")
            delegate?.selectedAlternativeItem(typeList[position], recylerType)
        }

    }


}

enum class AlternativeRecyclerType {
    sector,
    leftCountry,
    rightCountry
}