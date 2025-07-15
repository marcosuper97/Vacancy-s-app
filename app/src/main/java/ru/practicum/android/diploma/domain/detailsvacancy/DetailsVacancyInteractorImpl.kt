package ru.practicum.android.diploma.domain.detailsvacancy

import ru.practicum.android.diploma.domain.models.VacancyDetails

class DetailsVacancyInteractorImpl(
    private val detailsVacancyRepository: DetailsVacancyRepository
) : DetailsVacancyInteractor {
    override suspend fun doRequest(vacancyId: String): VacancyDetails {
        return detailsVacancyRepository.doRequest(vacancyId)
    }
}
