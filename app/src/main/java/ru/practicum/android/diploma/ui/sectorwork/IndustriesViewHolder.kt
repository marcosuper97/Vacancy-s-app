package ru.practicum.android.diploma.ui.sectorwork

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesViewHolder(
    itemView: View,
    val onItemClickListener: (Industry) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val textItem: TextView = itemView.findViewById(R.id.industry)

    fun bind(industry: Industry) {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener(industry)
            }
        }
        textItem.text = industry.name
    }
}
