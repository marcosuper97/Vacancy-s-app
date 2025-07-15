package ru.practicum.android.diploma.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.VacanciesPreview

class VacanciesAdapter(
    private val onVacancyClicked: (String) -> Unit,
) : ListAdapter<VacanciesPreview, VacanciesViewHolder>(VacancyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacanciesViewHolder {
        val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacanciesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VacanciesViewHolder, position: Int) {
        val vacancy = getItem(position)
        holder.bind(vacancy)
        holder.itemView.setOnClickListener {
            onVacancyClicked(vacancy.vacancyId)
        }
    }

    class VacancyDiffCallback : DiffUtil.ItemCallback<VacanciesPreview>() {

        override fun areItemsTheSame(oldItem: VacanciesPreview, newItem: VacanciesPreview): Boolean {
            return oldItem.vacancyId == newItem.vacancyId
        }

        override fun areContentsTheSame(oldItem: VacanciesPreview, newItem: VacanciesPreview): Boolean {
            return oldItem == newItem
        }
    }
}
