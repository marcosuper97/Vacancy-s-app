package ru.practicum.android.diploma.domain.interactors

interface AreasInteractor {
    suspend fun getAreas()
    suspend fun getCountriesData()
    suspend fun getAreasOfCountry()
}
