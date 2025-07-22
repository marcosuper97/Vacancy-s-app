package ru.practicum.android.diploma.ui.sectorwork

import ru.practicum.android.diploma.domain.models.Industry

interface IndustryState {
    data class Content(val areas: List<Industry>, val showApplyButton:Boolean) : IndustryState
    data class Search(val areas: List<Industry>, val showApplyButton:Boolean) : IndustryState
    data class NotFound(val showApplyButton:Boolean) : IndustryState
    data class Error(val showApplyButton:Boolean) : IndustryState
    data class Loading(val showApplyButton:Boolean) : IndustryState
}
