package ru.practicum.android.diploma.data.dto.vacancy.vacancydetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.AddressDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.EmployerDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.EmploymentFormDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.ExperienceDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.SalaryDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancyelements.WorkFormatDto

@Serializable
data class VacancyDetailsDto(
    val name: String,
    val employer: EmployerDto,
    val address: AddressDto?,
    @SerialName("salary_range")
    val salary: SalaryDto?,
    val description: String,
    @SerialName("employment_form")
    val employmentForm: EmploymentFormDto?,
    @SerialName("work_format")
    val workFormat: List<WorkFormatDto>?,
    @SerialName("alternate_url")
    val linkUrl: String,
    val experience: ExperienceDto?
)
