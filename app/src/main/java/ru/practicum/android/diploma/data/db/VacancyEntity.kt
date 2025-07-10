package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "vacancies_table"
)
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val url: String,
    val name: String,
    val currency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val employer: String?,
    val employerLogo: String?,
    val area: String,
    val experience: String?,
    val workFormat: String?,
    val employmentForm: String?,
    val description: String,
    val city: String?,
    val street: String?,
    val building: String?,
    val addressDescription: String?,
    val keySkills: List<String>,
    val additionTime: Long
)

