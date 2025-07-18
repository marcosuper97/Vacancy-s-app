package ru.practicum.android.diploma.domain.models

data class Filters(
    val country: String?,
    val area: String?,
    val industry: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean?,
)
