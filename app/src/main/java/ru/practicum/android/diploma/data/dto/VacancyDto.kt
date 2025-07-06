package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName


data class VacancyDto(
    val id: String,
    val name: String,
    val employer: EmployerDto,
    val area: Area,
    val description: String,
    @SerialName("salary_range")
    val salaryRange:SalaryRangeDto,
    @SerialName("employment_form")
    private val _employmentForm: Map<String, String>,
    @SerialName("experience")
    private val _experience: Map<String, String>
) {
    val employmentName: String get() = _employmentForm["name"] ?: ""
    val experienceName: String get() = _experience["name"] ?: ""
}

data class EmployerDto(
    @SerialName("logo_urls")
    val logoUrls: LogoUrlDto,
    val name:String,
)

data class LogoUrlDto(
    @SerialName("90")
    val size90:String,
)

data class Area(
    val id: String,
    val name: String,
)
