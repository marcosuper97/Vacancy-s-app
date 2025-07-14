package ru.practicum.android.diploma.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.VacanciesPreview
import ru.practicum.android.diploma.util.dpToPx
import ru.practicum.android.diploma.util.formatSalary

class VacanciesViewHolder(
    private val binding: VacancyItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: VacanciesPreview) {
        val context = itemView.context
        Glide.with(context)
            .load(item.employerLogo)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(CORNERS, context)))
            .placeholder(R.drawable.employer_logo_placeholder)
            .into(binding.logo)

        binding.name.text =
            context.getString(R.string.vacancy_name_area, item.vacancyName, item.address)

        binding.salary.text = formatSalary(context, item.salaryFrom, item.salaryTo, item.currency)
    }
companion object {
    const val CORNERS = 12f
}
}
