package ru.practicum.android.diploma.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesInteractor
import ru.practicum.android.diploma.ui.main.addLoadingItem
import ru.practicum.android.diploma.ui.main.removeLoadingItem
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

    // добавим новое состояние для отслеживания загрузки пагинации
    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage

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
        if (_isLoadingNextPage.value) return //  если true, это означает, что загрузка следующей страницы уже запущена.
        // В этом случае функция searchNextPage() просто завершается (return), чтобы избежать одновременного запуска нескольких запросов на загрузку данных.
        // Это предотвращает возникновение race condition
        currentPage++
        latestQueryText?.let {
            _isLoadingNextPage.value = true  //  устанавливаем состояние загрузки (прогресс бар внизу списка)
            searchVacancies(it, currentPage) }
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

        if (page == 0) { //  первая загрузка
            _uiState.value = SearchVacanciesState.Loading // показываем центральный прогресс бар
        } else {
            _isLoadingNextPage.value = true //  показываем прогресс бар внизу списка
            _vacancies.value.addLoadingItem()
        }

        viewModelScope.launch {
            searchInteractor?.searchVacancies(queryText, page)?.collect { result ->
                result.onSuccess {vacanciesList ->
                    val newList = _vacancies.value.toMutableList()
                    if (page == 0) {
                        newList.clear() // очищаем начальный LoadingItem
                    }
                    newList.removeLoadingItem()  // удаляем loading item (индикатор загрузки) из списка, если он там есть

                    newList.addAll(vacanciesList.vacanciesList)
                    _vacancies.value = newList

                    _uiState.value = SearchVacanciesState.ShowContent(vacanciesList)
                    _isLoadingNextPage.value = false // состояние загрузки устанавливается в false, чтобы разрешить загрузку следующей страницы
                }

                result.onFailure {
                    _vacancies.value.removeLoadingItem()
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
