package ru.practicum.android.diploma.util

import android.widget.TextView
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

