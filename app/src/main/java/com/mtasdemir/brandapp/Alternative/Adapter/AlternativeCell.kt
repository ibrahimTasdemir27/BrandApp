package com.mtasdemir.brandapp.Alternative.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mtasdemir.brandapp.R



class AlternativeCell(var typeList: Array<String>, val isLeft: Boolean): RecyclerView.Adapter<AlternativeCell.AlternativeCellViewHolder>() {



    class AlternativeCellViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionLabel)
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

        if (isLeft) {
            holder.descriptionTextView.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        } else {
            holder.descriptionTextView.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        }
    }


}

enum class AlternativeRecyclerType {
    //sector,
    leftCountry,
    rightCountry
}