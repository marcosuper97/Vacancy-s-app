package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancySearchResponse(
    val found: Int,
    val items: List<VacancyDto>,
    @SerialName("per_page")
    val perPage: Int,
    val pages: Int,
    val page: Int,
) : Response()


