package ru.practicum.android.diploma.data.dto

data class SalaryRangeDto(
    val currency: String,
    val from: Int? = 0,
    val gross: Boolean,
    val to: Int? = 0
)
