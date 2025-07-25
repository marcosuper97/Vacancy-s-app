package ru.practicum.android.diploma.data.dto.industry

import kotlinx.serialization.Serializable

@Serializable
data class IndustryGroupDto(
    val id: String,
    val name: String,
    val industries: List<IndustryDto>
)
