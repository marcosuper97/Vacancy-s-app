package ru.practicum.android.diploma.presentation.placework

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.AreasInteractor

class PlaceWorkViewModel(
    private val areasInteractor: AreasInteractor
) : ViewModel() {

    init {
        Log.d("init start", "init")
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            areasInteractor.fetchData()

            launch{
                areasInteractor.regionsData.collect { data ->
                    Log.d("РЕГИОНЫ ГОТОВО", "${data.getOrNull()}")
                }
            }

            areasInteractor.countriesData.collect{
                    data ->
                Log.d("СТРАНЫ", "${data.getOrNull()}")
            }
        }
    }
}
