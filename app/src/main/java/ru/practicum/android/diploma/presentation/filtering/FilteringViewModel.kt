package ru.practicum.android.diploma.presentation.filtering

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Filters

class FilteringViewModel(private val interactor: FiltersInteractor) : ViewModel() {
    private val defaultFilter = Filters(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
    private val _screenState = MutableStateFlow<FilteringScreenState>(FilteringScreenState(defaultFilter))
    val screenState: StateFlow<FilteringScreenState> = _screenState
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            launch {
                interactor.getFilters().collect { filters ->
                    _screenState.value = FilteringScreenState(filters)
                }
            }
        }
    }

    fun clearAreas() {
        viewModelScope.launch {
            interactor.clearAreas()
        }
    }

    fun clearIndustry() {
        viewModelScope.launch {
            interactor.clearIndustry()
        }
    }

    fun updateNoSalary() {
        viewModelScope.launch {
            val noSalaryState = screenState.value.filters.onlyWithSalary
            if (noSalaryState == null || noSalaryState == false) {
                interactor.updateNoSalary(true)
            } else {
                interactor.updateNoSalary(false)
            }
        }
    }

    fun updateSalary(query: String) {
        searchJob?.cancel()
        if (query.isEmpty()) {
            viewModelScope.launch {
                interactor.updateSalary(null)
            }
            return
        }
        searchJob = viewModelScope.launch {
            delay(UPDATE_DELAY)
            interactor.updateSalary(query)
        }
    }

    suspend fun reset() {
        interactor.reset()
    }

    companion object {
        private const val UPDATE_DELAY = 1000L
    }
}
