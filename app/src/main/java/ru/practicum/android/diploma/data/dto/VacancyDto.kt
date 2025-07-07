package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName


data class VacancyDto(
    val id: String,
    val name: String,
    val employer: EmployerDto,
    val area: Area,
    @SerialName("salary_range")
    val salaryRange:SalaryRangeDto,
    private val _schedule:  Map<String, String>,
)  {
    val scheduleName: String get() = _schedule["name"] ?: ""
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
