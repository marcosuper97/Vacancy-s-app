package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName

data class VacancySearchRequest(
    val text: String,
    val area:String?,
    val industry: String?,
    val page: Int?,
    @SerialName ("per_page")
    val perPage: Int,
)
