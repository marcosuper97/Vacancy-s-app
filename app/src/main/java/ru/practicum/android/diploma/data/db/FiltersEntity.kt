package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filters")
data class FiltersEntity(
    @PrimaryKey
    val id: Int = 1,
    val country: String?,
    val area: String?,
    val industry: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean?
)

