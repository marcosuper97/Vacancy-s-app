package ru.practicum.android.diploma.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.VacancyEntity
import ru.practicum.android.diploma.data.dto.VacancyDto

interface FavoritesRepository {
    suspend fun insertVacancy(vacancy: VacancyEntity)
    suspend fun deleteVacancy(id: String)
    fun getAllVacancies(): Flow<List<VacancyDto>>
    fun getOneVacancy(id: String): Flow<VacancyDto>
    suspend fun isFavorite(id: String): Boolean
}
