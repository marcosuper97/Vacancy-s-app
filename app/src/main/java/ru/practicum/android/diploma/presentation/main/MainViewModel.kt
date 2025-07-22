package ru.practicum.android.diploma.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.repositories.SearchVacanciesInteractor
import ru.practicum.android.diploma.util.AppException
import ru.practicum.android.diploma.util.RecyclerViewItem
import ru.practicum.android.diploma.util.SearchVacanciesState

class MainViewModel(private var searchInteractor: SearchVacanciesInteractor) : ViewModel() {
    private var latestQueryText: String? = null
    private var currentPage = 0
    private var debounceJob: Job? = null // Для управления дебounced-поиском
    private var isSearchActive = false // Флаг для отслеживания активного поиска
    private var pagesOnResponse = 0

    private val _uiState = MutableStateFlow<SearchVacanciesState>(SearchVacanciesState.Default)
    val uiState = _uiState.asStateFlow()

    private val _vacancies = MutableStateFlow<MutableList<RecyclerViewItem>>(mutableListOf())
    val vacancies: StateFlow<MutableList<RecyclerViewItem>> = _vacancies

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage

    private val _filtersState = MutableStateFlow(false)
    val filtersState: StateFlow<Boolean> = _filtersState.asStateFlow()

    init {
        observeFilters()
    }

    private fun searchDebounce(changedText: String) {
        if (latestQueryText == changedText || isSearchActive) {
            return
        }
        latestQueryText = changedText
        currentPage = 0
        _vacancies.value = mutableListOf()

        // Отменяем предыдущий дебounced-поиск
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchVacancies(changedText, currentPage)
            currentPage = 1
        }
    }

    fun searchNextPage() {
        if (pagesOnResponse - 1 > currentPage) {
            if (_isLoadingNextPage.value || isSearchActive) {
                return
            }
            currentPage++
            latestQueryText?.let {
                _isLoadingNextPage.value = true
                addLoadingItem()
                searchVacancies(it, currentPage)
            }
        }
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
        _vacancies.value = mutableListOf()
        isSearchActive = false
        debounceJob?.cancel()
    }

    private fun searchVacancies(queryText: String, page: Int) {
        if (queryText.isEmpty()) {
            return
        }
        isSearchActive = true // Устанавливаем флаг перед поиском
        viewModelScope.launch {
            if (page == 0) {
                _uiState.value = SearchVacanciesState.Loading
            }

            searchInteractor.searchVacancies(queryText, page).collect { result ->
                result.onSuccess { vacanciesList ->
                    pagesOnResponse = vacanciesList.pages
                    removeLoadingItem()
                    val newList = vacanciesList.vacanciesList.map { RecyclerViewItem.VacancyItem(it) }
                    if (page == 0) {
                        _vacancies.value = newList.toMutableList()
                    } else {
                        _vacancies.value = (_vacancies.value + newList).toMutableList()
                    }
                    _uiState.value = SearchVacanciesState.ShowContent(vacanciesList)
                    _isLoadingNextPage.value = false
                    isSearchActive = false // Сбрасываем флаг после завершения
                }

                result.onFailure {
                    removeLoadingItem()
                    _uiState.value = when (result.exceptionOrNull()) {
                        is AppException.EmptyResult, is AppException.NotFound -> SearchVacanciesState.NothingFound
                        is AppException.NoInternetConnection -> SearchVacanciesState.NoInternet
                        else -> SearchVacanciesState.NetworkError
                    }
                    _isLoadingNextPage.value = false
                    isSearchActive = false // Сбрасываем флаг после завершения
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        debounceJob?.cancel()
    }

    private fun addLoadingItem() {
        if (_vacancies.value.none { it is RecyclerViewItem.LoadingItem }) {
            _vacancies.value = (_vacancies.value + RecyclerViewItem.LoadingItem).toMutableList()
        }
    }

    private fun removeLoadingItem() {
        _vacancies.value = _vacancies.value.filter { it !is RecyclerViewItem.LoadingItem }.toMutableList()
    }

    fun performSearch(queryText: String) {
        debounceJob?.cancel() // Отменяем любой запланированный дебounced-поиск
        isSearchActive = true
        latestQueryText = queryText
        currentPage = 0
        _vacancies.value = mutableListOf()
        searchVacancies(queryText, 0)
    }

    private fun observeFilters() {
        viewModelScope.launch {
            searchInteractor.thereIsFilters().collect { hasFilters ->
                _filtersState.value = hasFilters
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
