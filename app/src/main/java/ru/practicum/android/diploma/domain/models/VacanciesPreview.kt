package ru.practicum.android.diploma.domain.models

data class VacanciesPreview(
    val vacancyId: String,
    val vacancyName: String,
    val employerName: String,
    val employerLogo: String?,
    val address: String,
    val salaryFrom: String?,
    val salaryTo: String?,
    val currency: String?
)
