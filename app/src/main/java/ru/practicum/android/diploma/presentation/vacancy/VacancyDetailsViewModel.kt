package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractor
import ru.practicum.android.diploma.domain.interactors.FavoritesInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.common.CommandChannel

class VacancyDetailsViewModel(
    private val detailsVacancyInteractor: DetailsVacancyInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val vacancyId: String,
) : ViewModel() {

    init {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isFetching = true) }
                val vacancyDetails = detailsVacancyInteractor.doRequest(vacancyId)
                _uiState.update {
                    it.copy(
                        isFetching = false,
                        isError = false,
                        vacancyDetails = vacancyDetails.getOrNull()
                    )
                }
            } catch (e: CancellationException) {
                println(e)
                _uiState.update { it.copy(isFetching = false, isError = true) }
            }
        }

        viewModelScope.launch {
            favoritesInteractor.isFavorite(vacancyId).collect { isFavorite ->
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }

    }

    private val _uiState = MutableStateFlow(VacancyUiState())
    val uiState = _uiState.asStateFlow()

    private val _commands = CommandChannel<VacancyDetailsCommand>()
    val commands = _commands.receiveAsFlow()

    fun onBackClicked() {
        viewModelScope.launch {
            _commands.send(VacancyDetailsCommand.NavigateBack)
        }
    }

    fun onShareClick() {
        viewModelScope.launch {
            val vacancyDetails = uiState.value.vacancyDetails
            if (vacancyDetails != null) {
                _commands.send(VacancyDetailsCommand.NavigateToShare(vacancyDetails.linkUrl))
            }
        }
    }

    fun favoriteControl(vacancy: VacancyDetails) {
        viewModelScope.launch {
            val isFavoriteNow = uiState.value.isFavorite
            if (isFavoriteNow) {
                favoritesInteractor.deleteVacancy(vacancyId)
            } else {
                favoritesInteractor.insertVacancy(vacancy)
            }
        }
    }
}
