package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 4,
    entities = [VacancyEntity::class, FiltersEntity::class]
)
abstract class DataBase : RoomDatabase() {
    abstract fun vacanciesDao(): VacanciesDao
    abstract fun filtersDao(): FiltersDao
}

