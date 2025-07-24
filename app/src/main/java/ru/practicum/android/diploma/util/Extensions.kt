package ru.practicum.android.diploma.util

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.db.FiltersEntity
import ru.practicum.android.diploma.data.dto.area.AreaDto
import ru.practicum.android.diploma.data.dto.industry.IndustryDto
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Filters
import ru.practicum.android.diploma.domain.models.Industry

fun TextView.setVacanciesCountText(count: Long) {
    this.text = String.format(
        context.getString(R.string.found),
        context.resources.getQuantityString(
            R.plurals.vacancies_count,
            count.toInt(),
            count
        )
    )
}

fun AreaDto.toModel(): Areas {
    return Areas(
        id = this.id,
        parentId = this.parentId,
        name = this.name,
        areas = this.areas.map { it.toModel() }
    )
}

fun IndustryDto.toModel(): Industry {
    return Industry(
        id = this.id,
        name = this.name
    )
}

fun Filters.toEntity(): FiltersEntity {
    return FiltersEntity(
        country = this.country,
        countryId = this.countryId,
        area = this.area,
        areaId = this.areaId,
        industry = this.industry,
        industryId = this.industryId,
        salary = this.salary,
        onlyWithSalary = this.onlyWithSalary
    )
}

object FragmentSnackExtension {
    fun Fragment.showSnackBar(
        rootView: View,
        message: String,
        @LayoutRes layoutRes: Int = R.layout.snack_bar,
        duration: Int = Snackbar.LENGTH_LONG
    ) {
        val customView = LayoutInflater.from(requireContext()).inflate(layoutRes, null).apply {
            findViewById<TextView>(R.id.snackbar_text).text = message
        }

        Snackbar.make(rootView, "", duration).apply {
            view.setBackgroundColor(Color.TRANSPARENT)
            view.setPadding(0, 0, 0, 0)
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let { params ->
                params.setMargins(16.dpToPx(), 0, 16.dpToPx(), 68.dpToPx()) // Пример: 32dp снизу
                view.layoutParams = params
            }
            (view as ViewGroup).apply {
                removeAllViews()
                addView(customView)
            }
        }.show()
    }
}
