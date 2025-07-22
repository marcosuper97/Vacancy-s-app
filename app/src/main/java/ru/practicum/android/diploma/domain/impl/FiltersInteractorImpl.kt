package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import ru.practicum.android.diploma.domain.interactors.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Filters
import ru.practicum.android.diploma.domain.repositories.FiltersRepository
import ru.practicum.android.diploma.util.toEntity

class FiltersInteractorImpl(private val filtersRepository: FiltersRepository) : FiltersInteractor {
    override fun getFilters(): Flow<Filters> = filtersRepository.getFilters()

    override suspend fun clearAreas() {
        val filterCurrent = getFilters().first()
        val newFilters = filterCurrent.copy(
            country = null,
            countryId = null,
            area = null,
            areaId = null,
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override suspend fun clearIndustry() {
        val filterCurrent = getFilters().first()
        val newFilters = filterCurrent.copy(
            industryId = null,
            industry = null,
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override suspend fun updateSalary(salary: String?) {
        val filterCurrent = getFilters().first()
        val newFilters = filterCurrent.copy(
            salary = salary,
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override suspend fun updateNoSalary(onlyWithSalary: Boolean) {
        val filterCurrent = getFilters().first()
        val newFilters = filterCurrent.copy(
            onlyWithSalary = onlyWithSalary,
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override suspend fun reset() = filtersRepository.reset()
}
