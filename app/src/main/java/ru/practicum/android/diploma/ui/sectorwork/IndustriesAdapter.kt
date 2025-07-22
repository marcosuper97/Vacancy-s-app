package ru.practicum.android.diploma.ui.sectorwork

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesAdapter(
    private val onIndustrySelected: (Industry) -> Unit
) : RecyclerView.Adapter<IndustriesViewHolder>() {

    private var industries: MutableList<Industry> = mutableListOf()
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sector_work_item, parent, false)
        return IndustriesViewHolder(view) { clickedPosition ->
            if (selectedPosition != clickedPosition) {
                val oldSelected = selectedPosition
                selectedPosition = clickedPosition
                industries = industries.mapIndexed { index, industry ->
                    if (index == selectedPosition) {
                        industry.copy(select = true)
                    } else {
                        industry.copy(select = false)
                    }
                }.toMutableList()
                if (oldSelected != RecyclerView.NO_POSITION) notifyItemChanged(oldSelected)
                notifyItemChanged(selectedPosition)
                onIndustrySelected(industries[selectedPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: IndustriesViewHolder, position: Int) {
        holder.bind(industries[position])
    }

    override fun getItemCount(): Int = industries.size

    fun update(newIndustries: List<Industry>) {
        industries = newIndustries.toMutableList()
        selectedPosition = industries.indexOfFirst { it.select }
        notifyDataSetChanged()
    }
}
