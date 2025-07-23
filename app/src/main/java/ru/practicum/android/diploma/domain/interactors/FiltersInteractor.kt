package ru.practicum.android.diploma.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Filters

interface FiltersInteractor {
    fun getFilters(): Flow<Filters>
    suspend fun clearAreas()
    suspend fun clearIndustry()
    suspend fun updateSalary(salary: String?)
    suspend fun updateNoSalary(onlyWithSalary: Boolean)
    suspend fun reset()
}
