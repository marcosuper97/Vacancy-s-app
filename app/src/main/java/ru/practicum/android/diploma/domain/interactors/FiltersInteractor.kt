package ru.practicum.android.diploma.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filters

interface FiltersInteractor {
    fun getFilters(): Flow<Filters>
    suspend fun update(filters: Filters)
    suspend fun reset()
    fun thereIsFilters(): Flow<Boolean>
}
