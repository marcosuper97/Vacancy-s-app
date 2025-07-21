package ru.practicum.android.diploma.ui.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Areas

class CountryAdapter(private val onTrackClickListener: (Areas) -> Unit) :
    RecyclerView.Adapter<CountryViewHolder>() {
    var countries: List<Areas> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_work_item, parent, false)
        return CountryViewHolder(view, onTrackClickListener)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }
}
