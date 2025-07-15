package ru.practicum.android.diploma.data.dto.vacancy.vacancyelements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployerLogoDto(
    @SerialName("90")
    val path: String,
)
