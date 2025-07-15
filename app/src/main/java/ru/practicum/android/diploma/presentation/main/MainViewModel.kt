package ru.practicum.android.diploma.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesInteractor
import ru.practicum.android.diploma.util.AppException
import ru.practicum.android.diploma.util.LoadingItem
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.SearchVacanciesState
import ru.practicum.android.diploma.util.debounce

class MainViewModel(private var searchInteractor: SearchVacanciesInteractor?) : ViewModel() {
    private var latestQueryText: String? = null
    private var currentPage = 0

    private val _uiState = MutableStateFlow<SearchVacanciesState>(SearchVacanciesState.Default)
    val uiState = _uiState.asStateFlow()

    //  список для хранения данных
    private val _vacancies = MutableStateFlow<MutableList<Any>>(mutableListOf())
    val vacancies: StateFlow<MutableList<Any>> = _vacancies

    private val vacanciesSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { request -> searchVacancies(request, currentPage) }

    private fun searchDebounce(changedText: String) {
        if (latestQueryText == changedText) return
        latestQueryText = changedText
        currentPage = 0
        _vacancies.value = mutableListOf() // очищаем список перед новым поиском
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
        _vacancies.value = mutableListOf() // очищаем список при очистке поиска
    }

    private fun searchVacancies(queryText: String, page: Int) {
        if (queryText.isEmpty()) return

        _uiState.value = SearchVacanciesState.Loading
        val currentList = _vacancies.value.toMutableList()
        currentList.add(LoadingItem) //  добавляем LoadingItem перед запросом
        _vacancies.value = currentList

        viewModelScope.launch {
            searchInteractor?.searchVacancies(queryText, page)?.collect { result ->
                result.onSuccess {vacanciesList ->
                    val currentList = _vacancies.value.toMutableList()
                    currentList.removeAll { it is LoadingItem } //  удаляем LoadingItem
                    currentList.addAll(vacanciesList.vacanciesList) // добавляем новые вакансии
                    _vacancies.value = currentList
                    _uiState.value = SearchVacanciesState.ShowContent(vacanciesList)
                }

                result.onFailure {
                    //  удаляем LoadingItem при ошибке
                    val currentList = _vacancies.value.toMutableList()
                    currentList.removeAll { it is LoadingItem }
                    _vacancies.value = currentList
                    _uiState.value = when (result.exceptionOrNull()) {
                        is AppException.EmptyResult, is AppException.NotFound -> SearchVacanciesState.NothingFound
                        is AppException.NoInternetConnection -> SearchVacanciesState.NoInternet
                        else -> SearchVacanciesState.NetworkError
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
