package ru.practicum.android.diploma.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesInteractor
import ru.practicum.android.diploma.util.AppException
import ru.practicum.android.diploma.util.SearchVacanciesState
import ru.practicum.android.diploma.util.debounce

class MainViewModel(private var searchInteractor: SearchVacanciesInteractor?) : ViewModel(

) {
    private var latestQueryText: String? = null

    private val _uiState = MutableStateFlow<SearchVacanciesState>(SearchVacanciesState.Default)
    val uiState = _uiState.asStateFlow()

    private val vacanciesSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { request -> searchVacancies(request) }

    fun searchDebounce(changedText: String) {
        if (latestQueryText == changedText) return
        latestQueryText = changedText
        vacanciesSearchDebounce(changedText)
    }

    private fun searchVacancies(queryText: String) {
        if (queryText.isNotEmpty()) {
            _uiState.value = SearchVacanciesState.Loading

            viewModelScope.launch {
                searchInteractor?.searchVacancies(queryText, 0)?.collect { result ->
                    if (result.isSuccess) {

                        val content = result.getOrNull()

                        if (content == null)
                            _uiState.value = SearchVacanciesState.NothingFound
                        else
                            _uiState.value = SearchVacanciesState.ShowContent(content)

                    } else {
                        when (result.exceptionOrNull()) {
                            is AppException.EmptyResult -> _uiState.value = SearchVacanciesState.NothingFound
                            else -> _uiState.value = SearchVacanciesState.NetworkError
                        }
                    }
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        searchInteractor = null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}
