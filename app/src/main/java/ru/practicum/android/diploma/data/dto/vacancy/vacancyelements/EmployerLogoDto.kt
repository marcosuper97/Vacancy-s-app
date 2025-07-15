package ru.practicum.android.diploma.data.dto.vacancy.vacancyelements

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class EmployerLogoDto(
    @SerialName("90")
    val path: String,
)
