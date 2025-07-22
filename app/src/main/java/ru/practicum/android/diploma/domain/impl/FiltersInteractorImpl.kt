package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.interactors.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Filters
import ru.practicum.android.diploma.domain.repositories.FiltersRepository
import ru.practicum.android.diploma.util.toEntity

class FiltersInteractorImpl(private val filtersRepository: FiltersRepository) : FiltersInteractor {
    override fun getFilters(): Flow<Filters> = filtersRepository.getFilters()

    override suspend fun update(filters: Filters) {
        filtersRepository.update(filters.toEntity())
    }

    override suspend fun reset() = filtersRepository.reset()

    override fun thereIsFilters(): Flow<Boolean> = filtersRepository.thereIsFilters()
}
