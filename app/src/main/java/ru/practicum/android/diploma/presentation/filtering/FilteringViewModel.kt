package ru.practicum.android.diploma.presentation.filtering

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Filters

class FilteringViewModel(private val interactor: FiltersInteractor) : ViewModel() {
    private val _screenState = MutableStateFlow<FilteringScreenState>(FilteringScreenState(null))
    val screenState: StateFlow<FilteringScreenState> = _screenState

    init {
        viewModelScope.launch {
            launch {
                interactor.getFilters().collect { filters ->
                    _screenState.value = FilteringScreenState(filters)
                }
            }
        }
    }

    fun clearAreas(){

    }

    fun clearIndustry(){

    }

    fun updateSalary(){

    }

    fun updateNoSalary(){

    }

    suspend fun reset() {
        interactor.reset()
    }
}
