package ru.practicum.android.diploma.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.FavoritesInteractor

class FavoritesViewModel(private val interactor: FavoritesInteractor) : ViewModel() {
    private val _screenState = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Empty)
    val screenState: StateFlow<FavoritesScreenState> = _screenState

    fun loadVacancies() {
        viewModelScope.launch {
            interactor
                .getAllVacancies()
                .onStart {
                    _screenState.value = FavoritesScreenState.Loading
                }
                .collect { vacancies ->
                    runCatching {
                        if (vacancies.isEmpty()) {
                            _screenState.value = FavoritesScreenState.Empty
                        } else {
                            _screenState.value = FavoritesScreenState.Content(vacancies)
                        }
                    }.onFailure {
                        _screenState.value = FavoritesScreenState.Error
                    }
                }
        }
    }

}
