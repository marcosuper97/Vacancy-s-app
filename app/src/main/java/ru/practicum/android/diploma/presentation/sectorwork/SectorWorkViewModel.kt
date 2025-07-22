package ru.practicum.android.diploma.presentation.sectorwork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.sectorwork.IndustryState

class SectorWorkViewModel(
    private val industryInteractor: IndustryInteractor
) : ViewModel() {
    private val _state = MutableStateFlow<IndustryState>(IndustryState.Loading(false))
    val state: StateFlow<IndustryState> get() = _state
    private var industryList: List<Industry> = emptyList()
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            industryInteractor.getIndustries()
                .onSuccess { industriesData ->
                    val applyButton = showApplyButton(industriesData)
                    _state.value = IndustryState.Content(industriesData, applyButton)
                    industryList = industriesData
                }
                .onFailure {
                    _state.value = IndustryState.Error(false)
                }
        }
    }

    fun searchIndustry(query: String) {
        searchJob?.cancel()
        if (query.isEmpty()) {
            val applyButton = showApplyButton(industryList)
            _state.value = IndustryState.Content(industryList, applyButton)
            return
        }
        searchJob = viewModelScope.launch {
            delay(500)
            val filteredList = filterIndustries(industryList, query)
            _state.value = if (filteredList.isEmpty()) {
                IndustryState.NotFound(false)
            } else {
                val applyButton = showApplyButton(filteredList)
                IndustryState.Search(filteredList, applyButton)
            }
        }
    }

    private fun showApplyButton(industries: List<Industry>): Boolean {
        return industries.any { it.select }
    }

    private fun filterIndustries(industries: List<Industry>, query: String): List<Industry> {
        return industries.filter { industry ->
            industry.name.contains(query, ignoreCase = true)
        }
    }

    fun industryUpdate(industry: Industry) {
        viewModelScope.launch {
            industryInteractor.updateIndustry(industry)
        }
    }
}
