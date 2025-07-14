package ru.practicum.android.diploma.domain.models

data class VacancyDetails(
    val vacancyId: String,
    val vacancyName: String,
    val employerName: String?,
    val employerLogo: String?,
    val address: String,
    val salaryFrom: String?,
    val salaryTo: String?,
    val currency: String,
    val workFormat: List<String>?,
    val employmentForm: String?,
    val experience: String?,
    val linkUrl: String,
    val description: String,
    val keySkills: List<String>,
    val isFavorite: Boolean = false
)
