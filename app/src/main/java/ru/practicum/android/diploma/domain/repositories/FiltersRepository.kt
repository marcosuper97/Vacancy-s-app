package ru.practicum.android.diploma.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.FiltersEntity
import ru.practicum.android.diploma.domain.models.Filters

interface FiltersRepository {
    fun getFilters(): Flow<Filters>
    suspend fun update(entity: FiltersEntity)
    suspend fun reset()
}
