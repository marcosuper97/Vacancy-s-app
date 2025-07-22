package ru.practicum.android.diploma.presentation.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.AreasInteractor
import ru.practicum.android.diploma.ui.country.CountryState
import ru.practicum.android.diploma.util.AppException

class CountryViewModel(
    private val areasInteractor: AreasInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<CountryState>(CountryState.Loading)
    val state: StateFlow<CountryState> get() = _state

    init {
        viewModelScope.launch {
            areasInteractor.countriesData.collect { data ->
                data.onSuccess { areasData ->
                    _state.value = CountryState.Content(areas = areasData)
                }
                data.onFailure {
                    when (data.exceptionOrNull()) {
                        is AppException -> {
                            _state.value = CountryState.Error
                        }
                    }
                }
            }
        }
    }

    fun countryUpdate(country: String, countryId: String) {
        viewModelScope.launch {
            areasInteractor.updateCountry(country, countryId)
        }
    }

}
