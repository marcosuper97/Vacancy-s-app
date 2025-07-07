package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "vacancies_table"
)
data class VacancyEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val currency: String,
    val salaryFrom: String,
    val salaryTo: String,
    val employer: String,
    val employerLogo: String,
    val area: String,
    val experience: String,
    val workFormat: String,
    val employmentForm: String,
    val description: String,
    val additionTime: Long = System.currentTimeMillis(),
)

