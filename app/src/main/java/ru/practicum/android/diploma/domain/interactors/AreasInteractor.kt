package ru.practicum.android.diploma.domain.interactors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Filters

interface AreasInteractor {
    val filters: Flow<Filters>
    val countriesData: SharedFlow<Result<List<Areas>>>
    val regionsData: SharedFlow<Result<List<Areas>>>
    suspend fun fetchData()
    suspend fun updateCountry(country: String?, countryId: String?)
    suspend fun updateRegion(area: Areas)
    suspend fun updateRegionWithParent(
        region: String?,
        regionId: String?,
        country: String?,
        countryId: String?
    )

    suspend fun cleanPlaceWork()
    suspend fun deleteRegion()
}
