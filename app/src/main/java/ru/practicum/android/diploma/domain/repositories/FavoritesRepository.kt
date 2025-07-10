package ru.practicum.android.diploma.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.VacancyEntity
import ru.practicum.android.diploma.domain.entity.Vacancy

interface FavoritesRepository {
    suspend fun insertVacancy(vacancy: VacancyEntity)
    suspend fun deleteVacancy(id: String)
    fun getAllVacancies(): Flow<List<Vacancy>>
    fun getOneVacancy(id: String): Flow<Vacancy>
    suspend fun isFavorite(id: String): Boolean
}
