package ru.practicum.android.diploma.ui.sectorwork

import ru.practicum.android.diploma.domain.models.Industry

interface IndustryState {
    data class Content(val areas: List<Industry>) : IndustryState
    data class Search(val areas: List<Industry>) : IndustryState
    data object NotFound : IndustryState
    data object Error : IndustryState
    data object Loading : IndustryState
}
