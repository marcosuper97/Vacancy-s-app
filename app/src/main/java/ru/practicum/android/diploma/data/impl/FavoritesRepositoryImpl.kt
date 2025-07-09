package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.DataBase
import ru.practicum.android.diploma.data.db.Converter
import ru.practicum.android.diploma.data.db.VacancyEntity
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.repositories.FavoritesRepository

class FavoritesRepositoryImpl(private val dataBase: DataBase) : FavoritesRepository {
    override suspend fun insertVacancy(vacancy: VacancyEntity) {
        dataBase.vacanciesDao().insertVacancy(vacancy)
    }

    override suspend fun deleteVacancy(id: Long) {
        dataBase.vacanciesDao().deleteVacancy(id)
    }

    override fun getAllVacancies(): Flow<List<VacancyDto>> =
        dataBase.vacanciesDao()
            .getAllVacancies()
            .map { entities -> mapEntityToDto(entities) }


    override fun getOneVacancy(id: Long): Flow<VacancyDto> =
        dataBase.vacanciesDao()
            .getOneVacancy(id)
            .map {vacancy -> Converter.map(vacancy)}

    override suspend fun isFavorite(id: Long): Boolean =
        dataBase.vacanciesDao().isFavorite(id)

    private fun mapEntityToDto(vacancies: List<VacancyEntity>): List<VacancyDto> {
        return vacancies.map { vacancy -> Converter.map(vacancy) }
    }
}
