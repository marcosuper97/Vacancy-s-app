package ru.practicum.android.diploma.data.dto.vacancy.vacanysearch

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.AddressDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.EmployerDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.SalaryDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.VacancyAreaDto

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class VacancySearchResponseDto(
    val page: Int,
    val pages: Int,
    val found: Long,
    @SerialName("items")
    val vacancies: List<VacancyPreviewDto>
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class VacancyPreviewDto(
    val id: String,
    val name: String,
    val employer: EmployerDto,
    val area: VacancyAreaDto,
    val address: AddressDto?,
    @SerialName("salary_range")
    val salary: SalaryDto?
)
