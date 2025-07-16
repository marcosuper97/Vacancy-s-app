package ru.practicum.android.diploma.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesInteractor
import ru.practicum.android.diploma.util.AppException
import ru.practicum.android.diploma.util.RecyclerViewItem
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.SearchVacanciesState
import ru.practicum.android.diploma.util.debounce

class MainViewModel(private var searchInteractor: SearchVacanciesInteractor?) : ViewModel() {
    private var latestQueryText: String? = null
    private var currentPage = 0
    private var searchJob: Job? = null
    private var isSearchPerformedByDone = false

    private val _uiState = MutableStateFlow<SearchVacanciesState>(SearchVacanciesState.Default)
    val uiState = _uiState.asStateFlow()

    private val _vacancies = MutableStateFlow<MutableList<RecyclerViewItem>>(mutableListOf())
    val vacancies: StateFlow<MutableList<RecyclerViewItem>> = _vacancies

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage

    private val vacanciesSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { request -> searchVacancies(request, currentPage) }

    private fun searchDebounce(changedText: String) {
        if (latestQueryText == changedText || isSearchPerformedByDone) return
        latestQueryText = changedText
        currentPage = 0
        _vacancies.value = mutableListOf()
        vacanciesSearchDebounce(changedText)
    }

    fun searchNextPage() {
        if (_isLoadingNextPage.value) return
        currentPage++
        latestQueryText?.let {
            _isLoadingNextPage.value = true
            addLoadingItem()
            searchVacancies(it, currentPage)
        }
    }

    fun onSearchTextChanged(queryText: String) {
        if (queryText.isEmpty()) {
            onClearSearchClicked()
        } else {
            searchDebounce(queryText) // Отправляем запрос всегда (даже после Done)
        }
    }

    fun onClearSearchClicked() {
        latestQueryText = ""
        _uiState.value = SearchVacanciesState.Default
        _vacancies.value = mutableListOf()
        isSearchPerformedByDone = false
    }

    private fun searchVacancies(queryText: String, page: Int) {
        if (queryText.isEmpty()) return
        viewModelScope.launch {
            if (page == 0) {
                _uiState.value = SearchVacanciesState.Loading
            }


            searchInteractor?.searchVacancies(queryText, page)?.collect { result ->
                result.onSuccess { vacanciesList ->
                    removeLoadingItem()
                    val newList = vacanciesList.vacanciesList.map { RecyclerViewItem.VacancyItem(it) }
                    if (page == 0) {
                        _vacancies.value = newList.toMutableList()
                    } else {
                        _vacancies.value = (_vacancies.value + newList).toMutableList()
                    }


                    _uiState.value = SearchVacanciesState.ShowContent(vacanciesList)
                    _isLoadingNextPage.value =
                        false
                }

                result.onFailure {
                    removeLoadingItem()
                    _uiState.value = when (result.exceptionOrNull()) {
                        is AppException.EmptyResult, is AppException.NotFound -> SearchVacanciesState.NothingFound
                        is AppException.NoInternetConnection -> SearchVacanciesState.NoInternet
                        else -> SearchVacanciesState.NetworkError
                    }
                    _isLoadingNextPage.value = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchInteractor = null
    }

    private fun addLoadingItem() {
        if (_vacancies.value.none { it is RecyclerViewItem.LoadingItem }) {
            _vacancies.value = (_vacancies.value + RecyclerViewItem.LoadingItem).toMutableList()
        }
    }

    private fun removeLoadingItem() {
        _vacancies.value = _vacancies.value.filter { it !is RecyclerViewItem.LoadingItem }.toMutableList()
    }

    private fun cancelDebounce() {
        searchJob?.cancel()
    }

    fun performSearch(queryText: String) {
        cancelDebounce()
        searchVacancies(queryText, 0)
        isSearchPerformedByDone = true
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
