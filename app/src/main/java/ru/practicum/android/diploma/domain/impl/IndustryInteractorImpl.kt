package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import ru.practicum.android.diploma.domain.interactors.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Filters
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repositories.FiltersRepository
import ru.practicum.android.diploma.domain.repositories.IndustryRepository
import ru.practicum.android.diploma.util.toEntity

class IndustryInteractorImpl(
    private val industryRepository: IndustryRepository,
    private val filtersRepository: FiltersRepository
) : IndustryInteractor {
    private val interactorScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override suspend fun getIndustries(): Result<List<Industry>> = industryRepository.getIndustries()
    val filters: SharedFlow<Filters> =
        filtersRepository.getFilters().shareIn(interactorScope, SharingStarted.Eagerly, 1)

    override suspend fun updateIndustry(industry: Industry) {
        val filterCurrent = filters.first()
        val newFilters = filterCurrent.copy(
            industry = industry.name,
            industryId = industry.id
        )
        filtersRepository.update(newFilters.toEntity())
    }
}
