package ru.practicum.android.diploma.data.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancySearchResponseDto(
    val page: Int,
    val pages: Int,
    val found: Long,
    @SerialName("items")
    val vacancies: List<VacancyPreviewDto>
)

@Serializable
data class VacancyPreviewDto(
    val id: String,
    val name: String,
    val employer: Employer,
    val address: AddressDto?,
    val salary: SalaryDto?
)

@Serializable
data class SalaryDto(
    val from : Long?,
    val to : Long?,
    val currency : String
)

@Serializable
data class Employer(
    val name: String,
    @SerialName("logo_urls")
    val employerLogo: EmployerLogoDto?
)

@Serializable
data class EmployerLogoDto(
    @SerialName("90")
    val path: String,
)

@Serializable
data class AddressDto(
    val city: String
)
