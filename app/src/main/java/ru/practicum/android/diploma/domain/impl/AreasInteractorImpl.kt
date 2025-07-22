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
                null -> filterRegionsOfCountry(areas, null)
                else -> filterRegionsOfCountry(areas, currentCountryId.countryId)
            }
        }
    }.shareIn(interactorScope, SharingStarted.Eagerly, 1)

    private fun flattenAreas(areas: List<Areas>): List<Areas> {
        return areas.flatMap { area ->
            listOf(area.copy(areas = emptyList())) + flattenAreas(area.areas)
        }
    }

    private fun filterRegionsOfCountry(areas: List<Areas>, targetParentId: String?): List<Areas> {
        return areas.flatMap { area ->
            when {
                area.parentId == null && targetParentId == null -> {
                    filterRegionsOfCountry(area.areas, area.id) // Продолжаем искать вложенные элементы
                }

                area.parentId == targetParentId -> {
                    listOf(area.copy(areas = emptyList())) + flattenAreas(area.areas)
                }

                area.areas.isNotEmpty() -> {
                    filterRegionsOfCountry(area.areas, targetParentId)
                }

                else -> emptyList()
            }
        }
    }

    fun findTopLevelArea(target: Areas, allAreas: List<Areas>): Areas? {
        val areaById = allAreas.associateBy { it.id }
        var current: Areas? = target
        while (current?.parentId != null) {
            current = areaById[current.parentId]
        }
        return current
    }

    override suspend fun fetchData() {
        areasRepository.fetchAreas()
    }

    override suspend fun updateCountry(country: String?, countryId: String?) {
        val filterCurrent = filters.first()
        val newFilters = filterCurrent.copy(
            country = country,
            countryId = countryId,
            area = null,
            areaId = null,
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override suspend fun updateRegion(area: Areas) {
        val filterCurrent = filters.first()
        if (filterCurrent.countryId != null) {
            val newFilters = filterCurrent.copy(
                area = area.name,
                areaId = area.id
            )
            filtersRepository.update(newFilters.toEntity())
        } else {
            val allAreas: List<Areas> = flattenAreas(areasRepository.areasData.first().getOrNull()!!)
            val parentCountry = findTopLevelArea(area, allAreas)
            val newFilters = filterCurrent.copy(
                area = area.name,
                areaId = area.id,
                country = parentCountry?.name,
                countryId = parentCountry?.id
            )
            filtersRepository.update(newFilters.toEntity())
        }
    }

    override suspend fun updateRegionWithParent(
        region: String?,
        regionId: String?,
        country: String?,
        countryId: String?
    ) {
        val filterCurrent = filters.first()
        val newFilters = filterCurrent.copy(
            area = region,
            areaId = regionId,
            country = country,
            countryId = countryId
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override suspend fun cleanPlaceWork() {
        val filterCurrent = filters.first()
        val newFilters = filterCurrent.copy(
            country = null,
            countryId = null,
            area = null,
            areaId = null
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override suspend fun deleteRegion() {
        val filterCurrent = filters.first()
        val newFilters = filterCurrent.copy(
            area = null,
            areaId = null
        )
        filtersRepository.update(newFilters.toEntity())
    }

    override fun close() {
        interactorScope.cancel()
    }
}
