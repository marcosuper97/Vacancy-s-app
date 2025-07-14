package ru.practicum.android.diploma.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesInteractor
import ru.practicum.android.diploma.util.AppException
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.SearchVacanciesState


class MainViewModel(private var searchInteractor: SearchVacanciesInteractor?) : ViewModel() {
    private var latestQueryText: String? = null
    private var currentPage = 0

    private val _uiState = MutableStateFlow<SearchVacanciesState>(SearchVacanciesState.Default)
    val uiState = _uiState.asStateFlow()

    private val vacanciesSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { request -> searchVacancies(request, currentPage) }

    private fun searchDebounce(changedText: String) {
        if (latestQueryText == changedText) return
        latestQueryText = changedText
        currentPage = 0
        vacanciesSearchDebounce(changedText)
    }

    fun searchNextPage() {
        currentPage++
        latestQueryText?.let { searchVacancies(it, currentPage) }
    }

    fun onSearchTextChanged(queryText: String) {
        if (queryText.isEmpty()) {
            onClearSearchClicked()
        } else {
            searchDebounce(queryText)
        }
    }

    fun onClearSearchClicked() {
        latestQueryText = ""
        _uiState.value = SearchVacanciesState.Default
    }

    private fun searchVacancies(queryText: String, page: Int) {
        if (queryText.isEmpty()) return

        _uiState.value = SearchVacanciesState.Loading

        viewModelScope.launch {
            searchInteractor?.searchVacancies(queryText, page)?.collect { result ->
                _uiState.value = when {
                    result.isSuccess && result.getOrNull() != null ->
                        SearchVacanciesState.ShowContent(result.getOrNull()!!)

                    result.isSuccess || result.exceptionOrNull() is AppException.EmptyResult ->
                        SearchVacanciesState.NothingFound

                    else ->
                        SearchVacanciesState.NetworkError
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
