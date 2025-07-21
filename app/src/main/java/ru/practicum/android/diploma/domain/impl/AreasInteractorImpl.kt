package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import okio.Closeable
import ru.practicum.android.diploma.data.db.FiltersEntity
import ru.practicum.android.diploma.domain.interactors.AreasInteractor
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.repositories.AreasRepository
import ru.practicum.android.diploma.domain.repositories.FiltersRepository
import ru.practicum.android.diploma.util.toEntity

class AreasInteractorImpl(
    private val areasRepository: AreasRepository,
    private val filtersRepository: FiltersRepository
) : AreasInteractor, Closeable {
    override val filters = filtersRepository.getFilters()
    private val interactorScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override val countriesData: SharedFlow<Result<List<Areas>>> = areasRepository.areasData.map { result ->
        result.map { areas ->
            areas.filter { it.parentId == null }
                .map { it.copy(areas = emptyList()) }
        }
    }.shareIn(interactorScope, SharingStarted.Eagerly, 1)

    override val regionsData: SharedFlow<Result<List<Areas>>> = combine(
        areasRepository.areasData,
        filters
    ) { areasData, currentCountryId ->
        areasData.map { areas ->
            when (currentCountryId.countryId) {
                null -> filterAllRegions(areas)
                else -> filterRegionsOfCountry(areas, currentCountryId.countryId)
            }
        }
    }.shareIn(interactorScope, SharingStarted.Eagerly, 1)

    private fun filterRegionsOfCountry(areas: List<Areas>, targetParentId: String): List<Areas> {
        return areas.flatMap { area ->
            when {
                area.parentId == targetParentId -> {
                    listOf(area.copy(areas = filterRegionsOfCountry(area.areas, area.id)))
                }

                area.areas.isNotEmpty() -> {
                    filterRegionsOfCountry(area.areas, targetParentId)
                }

                else -> emptyList()
            }
        }
    }

    private fun filterAllRegions(areas: List<Areas>): List<Areas> {
        return areas.filterNot { it.parentId == null }
            .map { area ->
                area.copy(areas = filterAllRegions(area.areas))
            }
    }

    override suspend fun fetchData() {
        areasRepository.fetchAreas()
    }

    override suspend fun updateCountry(country: String?, countryId: String?) {
        val filterCurrent = filters.first()
        val newFilters = filterCurrent.copy(
            country = country,
            countryId = countryId
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override suspend fun updateRegion(region: String?, regionId: String?) {
        val filterCurrent = filters.first()
        val newFilters = filterCurrent.copy(
            area = region,
            areaId = regionId
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override fun close() {
        interactorScope.cancel()
    }
}
