package com.mtasdemir.brandapp.Alternative.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mtasdemir.brandapp.R


class AlternativeBrandCell(var brandNameList: Array<String>, private val isLeft: Boolean): RecyclerView.Adapter<AlternativeBrandCell.AlternativeBrandCellViewHolder>() {


    class AlternativeBrandCellViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val brandNameTextView: TextView = view.findViewById(R.id.alternative_brand_name_textview)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlternativeBrandCellViewHolder {
        val holder = LayoutInflater.from(parent.context).inflate(R.layout.alernative_brand_cell, parent, false)
        return AlternativeBrandCellViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return brandNameList.count()
    }

    override fun onBindViewHolder(holder: AlternativeBrandCellViewHolder, position: Int) {
        holder.brandNameTextView.text = brandNameList[position]

        if (isLeft) {
            println("isLeft")
            holder.brandNameTextView.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        } else {
            println("isRight")
            holder.brandNameTextView.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        }
    }


}
