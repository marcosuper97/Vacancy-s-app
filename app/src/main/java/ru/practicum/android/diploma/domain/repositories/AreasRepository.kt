package ru.practicum.android.diploma.domain.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import ru.practicum.android.diploma.domain.models.Areas

interface AreasRepository {
    val areasData: SharedFlow<Result<List<Areas>>>
    suspend fun fetchAreas()
}
