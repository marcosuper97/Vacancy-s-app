package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancyDto(
    val id: String,
    val name: String,
    val employer: EmployerDto,
    val area: Area,
    @SerialName("salary_range")
    val salaryRange: SalaryRangeDto,
    @SerialName("work_format")
    val workFormat: WorkFormat,
)

@Serializable
data class EmployerDto(
    @SerialName("logo_urls")
    val logoUrls: LogoUrlDto,
    val name: String,
)

@Serializable
data class LogoUrlDto(
    @SerialName("90")
    val size90: String?,
)

@Serializable
data class Area(
    val id: String?,
    val name: String,
)

@Serializable
data class SalaryRangeDto(
    val currency: String,
    val from: Int? = 0,
    val gross: Boolean?,
    val to: Int? = 0,
)

@Serializable
data class WorkFormat(
    val name: String?,
)
