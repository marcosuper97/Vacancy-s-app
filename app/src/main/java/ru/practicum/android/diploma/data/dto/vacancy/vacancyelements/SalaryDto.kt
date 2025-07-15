package ru.practicum.android.diploma.data.dto.vacancy.vacancyelements

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class SalaryDto(
    val from: Int?,
    val to: Int?,
    val currency: String
)
