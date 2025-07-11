package ru.practicum.android.diploma.domain.searchvacancies

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacanciesList

interface SearchVacanciesInteractor {
    fun searchVacancies(textRequest: String, page: Int): Flow<Result<VacanciesList>>
}
