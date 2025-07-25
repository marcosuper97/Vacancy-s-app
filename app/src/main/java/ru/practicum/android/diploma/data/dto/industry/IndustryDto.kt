package ru.practicum.android.diploma.data.dto.industry

import kotlinx.serialization.Serializable

@Serializable
data class IndustryDto(
    val id: String,
    val name: String
)
