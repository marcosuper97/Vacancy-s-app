package ru.practicum.android.diploma.presentation.region

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.AreasInteractor
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.ui.region.RegionState
import ru.practicum.android.diploma.util.AppException

class RegionViewModel(
    private val areasInteractor: AreasInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<RegionState>(RegionState.Loading)
    val state: StateFlow<RegionState> get() = _state
    private var regionList: List<Areas> = emptyList()
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            launch {
                areasInteractor.regionsData.collect { data ->
                    data.onSuccess { areasData ->
                        _state.value = RegionState.Content(areas = areasData)
                        regionList = areasData
                    }
                    data.onFailure {
                        when (data.exceptionOrNull()) {
                            is AppException -> {
                                _state.value = RegionState.Error
                            }
                        }
                    }
                }
            }
        }
    }

    fun searchRegion(query: String) {
        searchJob?.cancel()
        if (query.isEmpty()) {
            _state.value = RegionState.Content(regionList)
            return
        }
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            val filteredList = filterRegions(regionList, query)
            _state.value = if (filteredList.isEmpty()) {
                RegionState.NotFound
            } else {
                RegionState.Search(filteredList)
            }
        }
    }

    private fun filterRegions(regions: List<Areas>, query: String): List<Areas> {
        return regions.filter { region ->
            region.name.contains(query, ignoreCase = true) ||
                region.areas.any { area ->
                    area.name.contains(query, ignoreCase = true)
                }
        }
    }

    fun regionUpdate(area: Areas) {
        viewModelScope.launch {
            areasInteractor.updateRegion(area)
        }
    }

    companion object {
        private const val SEARCH_DELAY = 500L
    }
}

