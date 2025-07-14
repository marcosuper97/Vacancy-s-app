package ru.practicum.android.diploma.domain.detailsvacancy

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetails

class DetailsVacancyInteractorImpl(
    private val detailsVacancyRepository: DetailsVacancyRepository
) :
    DetailsVacancyInteractor {
    override fun doRequest(vacancyId: String): Flow<Result<VacancyDetails>> =
        detailsVacancyRepository.doRequest(vacancyId)
}
