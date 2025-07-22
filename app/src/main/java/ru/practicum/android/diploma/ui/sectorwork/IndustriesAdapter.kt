package ru.practicum.android.diploma.ui.sectorwork

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesAdapter(private val onTrackClickListener: (Industry) -> Unit) :
    RecyclerView.Adapter<IndustriesViewHolder>() {
    var industries: List<Industry> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sector_work_item, parent, false)
        return IndustriesViewHolder(view, onTrackClickListener)
    }

    override fun getItemCount(): Int {
        return industries.size
    }

    fun update(industriesNew: List<Industry>) {
        industries = industriesNew
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: IndustriesViewHolder, position: Int) {
        holder.bind(industries[position])
    }
}
