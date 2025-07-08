package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancySearchRequest(
    val text: String,
    val area: String?,
    val industry: String?,
    val page: Int?,
    @SerialName("per_page")
    val perPage: Int?,
    @SerialName("only_with_salary")
    val onlyWithSalary: Boolean?
)
