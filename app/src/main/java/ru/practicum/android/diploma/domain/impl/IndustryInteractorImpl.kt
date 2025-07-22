package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.first
import ru.practicum.android.diploma.domain.interactors.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repositories.FiltersRepository
import ru.practicum.android.diploma.domain.repositories.IndustryRepository
import ru.practicum.android.diploma.util.toEntity

class IndustryInteractorImpl(
    private val industryRepository: IndustryRepository,
    private val filtersRepository: FiltersRepository
) : IndustryInteractor {
    override suspend fun getIndustries(): Result<List<Industry>> = industryRepository.getIndustries()
    private val filters = filtersRepository.getFilters()

    override suspend fun updateIndustry(industry: Industry){
        val filterCurrent = filters.first()
        val newFilters = filterCurrent.copy(
            industry = industry.name,
            industryId = industry.id
        )
        filtersRepository.update(newFilters.toEntity())
    }
}
