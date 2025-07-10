package ru.practicum.android.diploma.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.entity.Vacancy

interface FavoritesInteractor {

    suspend fun insertVacancy(vacancy: Vacancy)
    suspend fun deleteVacancy(id: String)
    fun getAllVacancies(): Flow<List<Vacancy>>
    fun getOneVacancy(id: String)
    suspend fun isFavorite(): Boolean

}
