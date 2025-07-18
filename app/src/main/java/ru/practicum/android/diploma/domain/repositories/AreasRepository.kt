package ru.practicum.android.diploma.domain.repositories

import ru.practicum.android.diploma.domain.models.Areas

interface AreasRepository {
    suspend fun getAreas(): Result<List<Areas>>
}
