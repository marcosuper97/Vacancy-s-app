package ru.practicum.android.diploma.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.domain.interactors.FavoritesInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavoritesViewModel(private val interactor: FavoritesInteractor) : ViewModel() {
    private val _screenState = MutableStateFlow<FavoritesScreenState>(FavoritesScreenState.Default)
    val screenState: StateFlow<FavoritesScreenState> = _screenState

    fun loadVacancies() {
        viewModelScope.launch {
            interactor
                .getAllVacancies()
                .catch { throwable ->
                    _screenState.value = FavoritesScreenState.Error
                }
                .collect { vacancies ->
                    _screenState.value = if (vacancies.isEmpty()) {
                        FavoritesScreenState.Empty
                    } else {
                        FavoritesScreenState.Content(vacancies)
                    }
                }
        }
    }

    suspend fun loadDetailedVacancy(id: String): VacancyDetails =
        withContext(Dispatchers.IO) {
            interactor.getVacancy(id)
        }
}
