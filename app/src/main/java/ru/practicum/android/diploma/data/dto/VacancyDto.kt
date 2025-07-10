package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancyDto(
    val id: String,
    val name: String,
    val employer: EmployerDto?,
    val area: Area,
    val experience: ExperienceDto?,
    @SerialName("salary_range")
    val salaryRange: SalaryRangeDto?,
    @SerialName("work_format")
    val workFormat: WorkFormat?,
    @SerialName("employment_form")
    val employmentFrom: EmploymentFormDto,
    val description: String,
    val address: String?,
    @SerialName("key_skills")
    val keySkills: List<String>
)

@Serializable
data class EmployerDto(
    @SerialName("logo_urls")
    val logoUrls: LogoUrlDto?,
    val name: String?,
)

@Serializable
data class LogoUrlDto(
    @SerialName("90")
    val size90: String?,
)

@Serializable
data class Area(
    val id: String,
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
    val name: String
)

@Serializable
data class ExperienceDto(
    val name: String
)

@Serializable
data class Address(
    val city: String?,
    val street: String?,
    val building: String?,
    val description: String?
)

@Serializable
data class EmploymentFormDto(
    val name: String
)


