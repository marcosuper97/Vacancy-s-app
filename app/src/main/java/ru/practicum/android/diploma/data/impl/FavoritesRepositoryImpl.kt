package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.Converter
import ru.practicum.android.diploma.data.db.DataBase
import ru.practicum.android.diploma.data.db.VacancyEntity
import ru.practicum.android.diploma.domain.entity.Vacancy
import ru.practicum.android.diploma.domain.repositories.FavoritesRepository

class FavoritesRepositoryImpl(private val dataBase: DataBase) : FavoritesRepository {

    override suspend fun insertVacancy(vacancy: VacancyEntity) {
        dataBase.vacanciesDao().insertVacancy(vacancy)
    }

    override suspend fun deleteVacancy(id: String) {
        dataBase.vacanciesDao().deleteVacancy(id)
    }

    override fun getAllVacancies(): Flow<List<Vacancy>> =
        dataBase.vacanciesDao()
            .getAllVacancies()
            .map { entitiesList -> entitiesList.map { entity -> Converter.map(entity) } }

    override fun getOneVacancy(id: String): Flow<Vacancy> =
        dataBase.vacanciesDao()
            .getOneVacancy(id)
            .map { vacancy -> Converter.map(vacancy) }

    override suspend fun isFavorite(id: String): Boolean =
        dataBase.vacanciesDao().isFavorite(id)
}
