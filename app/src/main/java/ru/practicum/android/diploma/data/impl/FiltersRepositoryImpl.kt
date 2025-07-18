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
                    area = entity.area,
                    industry = entity.industry,
                    salary = entity.salary,
                    onlyWithSalary = entity.onlyWithSalary
                )
            }

    override suspend fun update(entity: FiltersEntity) {
        dataBase.filtersDao()
            .update(entity)
    }
}
