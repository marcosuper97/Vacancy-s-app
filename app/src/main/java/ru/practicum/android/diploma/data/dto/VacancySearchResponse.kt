package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName

data class VacancySearchResponse (
    val found:Int,
    val items:List<VacancyDto>,
    @SerialName("per_page")
    val perPage:Int,
    val pages:Int,
    val page: Int,
) : Response()


