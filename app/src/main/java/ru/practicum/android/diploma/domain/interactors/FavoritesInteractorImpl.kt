package ru.practicum.android.diploma.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.Converter
import ru.practicum.android.diploma.domain.entity.Vacancy
import ru.practicum.android.diploma.domain.repositories.FavoritesRepository

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun insertVacancy(vacancy: Vacancy) {
        repository.insertVacancy(Converter.map(vacancy))
    }

    override suspend fun deleteVacancy(id: String) {
        repository.deleteVacancy(id)
    }

    override fun getAllVacancies(): Flow<List<Vacancy>> =
        repository.getAllVacancies()

    override fun getOneVacancy(id: String): Flow<Vacancy> =
        repository.getOneVacancy(id)

    override suspend fun isFavorite(id: String): Boolean =
        repository.isFavorite(id)
}
