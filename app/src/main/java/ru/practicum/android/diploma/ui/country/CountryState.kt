package ru.practicum.android.diploma.ui.country

import ru.practicum.android.diploma.domain.models.Areas

sealed interface CountryState {
    data class Content(val areas: List<Areas>) : CountryState
    data object Error : CountryState
    data object Loading : CountryState
}
