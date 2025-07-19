package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import okio.Closeable
import ru.practicum.android.diploma.domain.interactors.AreasInteractor
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.repositories.AreasRepository

class AreasInteractorImpl(
    private val areasRepository: AreasRepository
) : AreasInteractor, Closeable {
    private val countryId = MutableStateFlow<String?>(null)
    private val interactorScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val areasData: SharedFlow<Result<List<Areas>>> = areasRepository.areasData

    override val countriesData: SharedFlow<Result<List<Areas>>> = areasRepository.areasData.map { result ->
        result
            .onSuccess { areasData ->
                Result.success(
                    areasData
                        .filter { it.parentId == null }
                        .map { it.copy(areas = emptyList()) }
                )
            }
    }.shareIn(interactorScope, SharingStarted.Lazily, 1)

    override val regionsData: SharedFlow<Result<List<Areas>>> = combine(
        areasData,
        countryId
    ) { result, conuntryId ->
        result.onSuccess { areas ->
            val filteredAreas = when (countryId.value) {
                null -> filterAllRegions(areas)
                else -> filterRegionsOfCountry(areas, countryId.value!!)
            }
            Result.success(filteredAreas)
        }
    }.shareIn(interactorScope, SharingStarted.Lazily, 1)

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

    override fun close() {
        interactorScope.cancel()
    }
}
