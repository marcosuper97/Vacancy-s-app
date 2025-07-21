package ru.practicum.android.diploma.ui.country

import ru.practicum.android.diploma.domain.models.Areas

sealed interface AreasState{
    data class Content(val areas: List<Areas>):AreasState
    data object Error: AreasState
    data object Loading: AreasState
}
