package ru.practicum.android.diploma.ui.sectorwork

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesViewHolder(
    itemView: View,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val textItem: TextView = itemView.findViewById(R.id.industry)
    private val radioButton: RadioButton = itemView.findViewById(R.id.industry_rb)

    fun bind(industry: Industry) {
        textItem.text = industry.name
        radioButton.isChecked = industry.select

        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onClick(position)
            }
        }
    }
}
