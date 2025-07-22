package ru.practicum.android.diploma.ui.region

import ru.practicum.android.diploma.domain.models.Areas

sealed interface RegionState {
    data class Content(val areas: List<Areas>) : RegionState
    data class Search(val areas: List<Areas>) : RegionState
    data object NotFound : RegionState
    data object Error : RegionState
    data object Loading : RegionState
}
