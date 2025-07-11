package ru.practicum.android.diploma.domain.models

data class VacanciesPreview(
    val vacancyId: String,
    val vacancyName: String,
    val employerName: String,
    val city: String?,
    val salaryFrom: String?,
    val salaryTo: String?,
    val currency: String?
)
