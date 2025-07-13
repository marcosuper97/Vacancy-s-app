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
    private var currentPage = 0

    private val _uiState = MutableStateFlow<SearchVacanciesState>(SearchVacanciesState.Default)
    val uiState = _uiState.asStateFlow() // на этот стейт подписываемся в фрагменте для отображения
    // состояния экрана

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

    fun searchNextPage() { //вызываем когда долистали до последнего элемента recycler
        currentPage++
        latestQueryText?.let { searchVacancies(it, currentPage) }
    }

    fun onSearchTextChanged(queryText: String) { //вызываем, когда поменялся текст в поле поиска
        if (queryText.isEmpty())
            onClearSearchClicked()
        else {
            searchDebounce(queryText)
        }
    }

    fun onClearSearchClicked() { //вызываем когда нажали крестик в поле поиска
        latestQueryText = ""
        _uiState.value = SearchVacanciesState.Default
    }

    private fun searchVacancies(queryText: String, page: Int) {

        if (queryText.isNotEmpty()) {
            _uiState.value = SearchVacanciesState.Loading

            viewModelScope.launch {
                searchInteractor?.searchVacancies(queryText, page)?.collect { result ->
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
