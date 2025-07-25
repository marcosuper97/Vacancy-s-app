package ru.practicum.android.diploma.presentation.placework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.AreasInteractor

class PlaceWorkViewModel(
    private val areasInteractor: AreasInteractor,
) : ViewModel() {
    private val _state = MutableStateFlow(PlaceWorkState())
    val state: StateFlow<PlaceWorkState> get() = _state

    init {
        viewModelScope.launch {
            launch {
                areasInteractor.filters.collect { data ->
                    _state.value = _state.value.copy(
                        country = data.country,
                        area = data.area
                    )
                }
            }
            launch { areasInteractor.fetchData() }
        }
    }

    fun deleteRegion() {
        viewModelScope.launch {
            areasInteractor.deleteRegion()
        }
    }

    fun deleteCountry() {
        viewModelScope.launch {
            areasInteractor.cleanPlaceWork()
        }
    }
}
