package ru.practicum.android.diploma.domain.interactors

import kotlinx.coroutines.flow.SharedFlow
import ru.practicum.android.diploma.domain.models.Areas

interface AreasInteractor {
    val countriesData: SharedFlow<Result<List<Areas>>>
    val regionsData: SharedFlow<Result<List<Areas>>>
    suspend fun fetchData()
}
