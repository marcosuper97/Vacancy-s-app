package ru.practicum.android.diploma.presentation.favorites

import ru.practicum.android.diploma.domain.models.VacanciesPreview

interface FavoritesScreenState {
    data class Content(
        val vacancies: List<VacanciesPreview>,
    ) : FavoritesScreenState

    data object Loading : FavoritesScreenState
    data object Empty : FavoritesScreenState
    data object Error : FavoritesScreenState
}
