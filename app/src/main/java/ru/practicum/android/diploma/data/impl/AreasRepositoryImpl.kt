package ru.practicum.android.diploma.data.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.area.AreaDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.repositories.AreasRepository
import ru.practicum.android.diploma.util.toModel

class AreasRepositoryImpl(
    private val networkClient: NetworkClient,
    private val accessAreas: Set<String>
) : AreasRepository {
    private val _areasData = MutableSharedFlow<Result<List<Areas>>>(replay = 1)
    override val areasData: SharedFlow<Result<List<Areas>>> = _areasData

    override suspend fun fetchAreas() {
        Log.d("AreasRepository", "Fetching areas")
        val data = networkClient.getAreas().map { dto ->
            val filtered = areasFiltered(dto)
            Log.d("AreasRepository", "Filtered areas: ${filtered.size}")
            filtered.map { areaDto ->
                areaDto.toModel()
            }
        }
        Log.d("AreasRepository", "Emitting areas: success=${data.isSuccess}, size=${data.getOrNull()?.size}")
        _areasData.emit(data)
    }

    private suspend fun areasFiltered(areas: List<AreaDto>): List<AreaDto> {
        return withContext(Dispatchers.IO) {
            areas
                .filter { dto ->
                    accessAreas.any { allowed ->
                        dto.name.contains(allowed, ignoreCase = true)
                    }
                }
                .sortedBy { dto ->
                    accessAreas.indexOfFirst { allowed ->
                        dto.name.contains(allowed, ignoreCase = true)
                    }
                }
        }
    }
}
