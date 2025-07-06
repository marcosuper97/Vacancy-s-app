package ru.practicum.android.diploma.data.db

import androidx.room.Database

class DataBase {

    @Database(
        version = 1,
        entities = [VacancyEntity::class]
    )
}
