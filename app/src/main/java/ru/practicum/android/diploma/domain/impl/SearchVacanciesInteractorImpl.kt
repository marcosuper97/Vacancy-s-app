package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacanciesList
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesInteractor
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesRepository

class SearchVacanciesInteractorImpl(
    private val repository: SearchVacanciesRepository
) : SearchVacanciesInteractor {

    override fun searchVacancies(textRequest: String, page: Int): Flow<Result<VacanciesList>> {
        return repository.doRequest(textRequest, page)
    }
}
