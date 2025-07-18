package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.DataBase
import ru.practicum.android.diploma.data.db.FiltersEntity
import ru.practicum.android.diploma.domain.models.Filters
import ru.practicum.android.diploma.domain.repositories.FiltersRepository

class FiltersRepositoryImpl(private val dataBase: DataBase) : FiltersRepository {
    override fun getFilters(): Flow<Filters> =
        dataBase.filtersDao()
            .getFilters()
            .map { entity ->
                Filters(
                    country = entity.country,
                    countryId = entity.countryId,
                    area = entity.area,
                    areaId = entity.areaId,
                    industry = entity.industry,
                    industryId = entity.industryId,
                    salary = entity.salary,
                    onlyWithSalary = entity.onlyWithSalary
                )
            }

    override suspend fun update(entity: FiltersEntity) {
        dataBase.filtersDao()
            .update(entity)
    }

    override suspend fun reset() {
        dataBase.filtersDao()
            .update(
                FiltersEntity(
                    id = 1,
                    country = null,
                    countryId = null,
                    area = null,
                    areaId = null,
                    industry = null,
                    industryId = null,
                    salary = null,
                    onlyWithSalary = null
                )
            )
    }

}
