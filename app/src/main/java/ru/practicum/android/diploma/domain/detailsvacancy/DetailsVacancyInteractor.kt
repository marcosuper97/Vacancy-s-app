package ru.practicum.android.diploma.domain.detailsvacancy

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface DetailsVacancyInteractor {
    fun doRequest(vacancyId: String): Flow<Result<VacancyDetails>>
}
