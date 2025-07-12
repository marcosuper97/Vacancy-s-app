package ru.practicum.android.diploma.data.dto

import kotlinx.serialization.SerialName

data class VacancyDetailsResponse(
    val id: String,
    val name: String,
    val employer: EmployerDto,
    val area: VacancyAreaDto,
    val description: String,
    @SerialName("salary_range")
    val salaryRange: SalaryRangeDto,
    @SerialName("employment_form")
    private val employmentForm: Map<String, String>,
    @SerialName("experience")
    private val experience: Map<String, String>
) : Response() {
    val employmentName: String get() = employmentForm["name"] ?: ""
    val experienceName: String get() = experience["name"] ?: ""
}

