package ru.practicum.android.diploma.presentation.region

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.AreasInteractor
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.ui.country.AreasState
import ru.practicum.android.diploma.util.AppException

class RegionViewModel(
    private val areasInteractor: AreasInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<AreasState>(AreasState.Loading)
    val state: StateFlow<AreasState> get() = _state
    private var countriesList: List<Areas> = emptyList()

    init {
        viewModelScope.launch {
            launch {
                areasInteractor.regionsData.collect { data ->
                    data.onSuccess { areasData ->
                        _state.value = AreasState.Content(areas = areasData)
                    }
                    data.onFailure {
                        when (data.exceptionOrNull()) {
                            is AppException -> {
                                _state.value = AreasState.Error
                            }
                        }
                    }
                }
            }

            launch {
                viewModelScope.launch {
                    areasInteractor.countriesData.collect { data ->
                        data.onSuccess { areasData ->
                            countriesList = areasData
                        }
                        data.onFailure {
                            countriesList = emptyList()
                        }
                    }
                }
            }
        }
    }

    private fun List<Areas>.containsInId(searchString: String): Boolean {
        return this.any { it.id.contains(searchString, ignoreCase = true) }
    }

    fun regionUpdate(area: Areas) {
        viewModelScope.launch {
            if(countriesList.containsInId(area.parentId!!)){
                areasInteractor.updateRegion(area.name, area.id)
                areasInteractor.updateCountry()
            }
            areasInteractor.updateRegion(region, regionId)
        }
    }
}
