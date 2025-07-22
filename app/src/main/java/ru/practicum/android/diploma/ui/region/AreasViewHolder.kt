package ru.practicum.android.diploma.ui.region

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Areas

class AreasViewHolder(
    itemView: View,
    val onItemClickListener: (Areas) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val textItem: TextView = itemView.findViewById(R.id.text_item)

    fun bind(area: Areas) {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener(area)
            }
        }
        textItem.text = area.name
    }
}
