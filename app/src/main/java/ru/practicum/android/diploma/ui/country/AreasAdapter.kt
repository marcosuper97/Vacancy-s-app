package ru.practicum.android.diploma.ui.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Areas

class AreasAdapter(private val onTrackClickListener: (Areas) -> Unit) :
    RecyclerView.Adapter<AreasViewHolder>() {
    var countries: List<Areas> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_work_item, parent, false)
        return AreasViewHolder(view, onTrackClickListener)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun update(areas: List<Areas>) {
        countries = areas
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AreasViewHolder, position: Int) {
        holder.bind(countries[position])
    }
}
