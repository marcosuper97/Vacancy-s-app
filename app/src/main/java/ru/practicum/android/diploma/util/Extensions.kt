package ru.practicum.android.diploma.util

import android.widget.TextView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.area.AreaDto
import ru.practicum.android.diploma.domain.models.Areas

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

fun AreaDto.toDomain(): Areas {
    return Areas(
        id = this.id,
        parentId = this.parentId,
        name = this.name,
        areas = this.areas.map { it.toDomain() }
    )
}

