package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.area.AreaDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.repositories.AreasRepository
import ru.practicum.android.diploma.util.toModel

class AreasRepositoryImpl(private val networkClient: NetworkClient) : AreasRepository {

    override suspend fun getAreas(): Result<List<Areas>> {
        return networkClient.getAreas().map { dto ->
            areasFiltered(dto)
                .map { areaDto ->
                    areaDto.toModel()
                }
        }
    }

    private suspend fun areasFiltered(areas: List<AreaDto>): List<AreaDto> {
        return withContext(Dispatchers.IO) {
            areas.filter { dto ->
                ACCESS_AREAS.any { allowed ->
                    dto.name.contains(allowed, ignoreCase = true)
                }
            }
        }
    }

    companion object {
        private val ACCESS_AREAS: Set<String> = setOf(
            "Россия",
            "Украина",
            "Казахстан",
            "Азербайджан",
            "Беларусь",
            "Грузия",
            "Кыргызстан",
            "Узбекистан",
            "Другие регионы"
        )
    }
}
