package ru.practicum.android.diploma.domain.detailsvacancy

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacanciesList

interface DetailsVacancyRepository {
    fun doRequest(vacancyId: String): Flow<Result<VacanciesList>>
}
