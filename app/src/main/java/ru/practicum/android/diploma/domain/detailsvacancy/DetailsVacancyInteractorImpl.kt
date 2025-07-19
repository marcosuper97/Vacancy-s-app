package ru.practicum.android.diploma.domain.detailsvacancy

import kotlinx.coroutines.flow.first
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.repositories.FavoritesRepository

class DetailsVacancyInteractorImpl(
    private val detailsVacancyRepository: DetailsVacancyRepository,
    private val favoritesRepository: FavoritesRepository
) : DetailsVacancyInteractor {
    override suspend fun doRequest(vacancyId: String): Result<VacancyDetails> {
        val isFavorite = favoritesRepository.isFavorite(vacancyId).first()
        return if (isFavorite) {
            Result.success(favoritesRepository.getVacancy(vacancyId))
        } else {
            detailsVacancyRepository.doRequest(vacancyId)
        }
    }
}
