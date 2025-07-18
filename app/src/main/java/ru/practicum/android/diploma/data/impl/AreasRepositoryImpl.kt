package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.repositories.AreasRepository

class AreasRepositoryImpl():AreasRepository {
    override suspend fun getAreas(): Result<List<Areas>> {
        TODO("Not yet implemented")
    }
}
