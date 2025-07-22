package ru.practicum.android.diploma.presentation.filtering

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.FiltersInteractor
import ru.practicum.android.diploma.domain.models.Filters

class FilteringViewModel(private val interactor: FiltersInteractor) : ViewModel() {
    private val _screenState = MutableStateFlow<FilteringScreenState>(FilteringScreenState())
    val screenState: StateFlow<FilteringScreenState> = _screenState

    init {
        observeFilters()
    }

    private fun observeFilters() {
        viewModelScope.launch {
            launch {
                interactor.getFilters().collect { filters ->
                    _screenState.value = _screenState.value.copy(filters = filters)
                }
            }
            launch {
                interactor.thereIsFilters().collect { thereIs ->
                    _screenState.value = _screenState.value.copy(thereIsFilters = thereIs)
                }
            }
        }
    }


    private fun updateFilters(transform: (Filters) -> Filters) {
        viewModelScope.launch {
            val current = _screenState.value.filters
            val updated = transform(current!!)
            interactor.update(updated)
            _screenState.value = _screenState.value.copy(filters = updated)
        }
    }

    fun updateArea(area: String?) = updateFilters { it.copy(area = area) }

    fun updateIndustry(industry: String?) = updateFilters { it.copy(industry = industry) }

    fun updateSalary(salary: Int?) = updateFilters { it.copy(salary = salary?.toString()) }

    fun updateOnlyWithSalary(checked: Boolean?) = updateFilters { it.copy(onlyWithSalary = checked) }

    fun clearSalary() = updateFilters { it.copy(salary = null) }

    suspend fun reset() {
        interactor.reset()
    }
}
