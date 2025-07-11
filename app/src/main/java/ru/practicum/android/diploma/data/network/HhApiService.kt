package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.search.VacancySearchResponseDto

interface HhApiService {
    //https://api.hh.ru/vacancies?page=1&per_page=20&text=Лабубу
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Vacancy's app/1.0 anannat@yandex.ru"
    )
    @GET("vacancies")
    suspend fun searchVacancies(
        @QueryMap options: Map<String, String>
    ): VacancySearchResponseDto

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsResponse

    @GET("industries")
    suspend fun getIndustries(): List<IndustryDto>

    @GET("areas")
    suspend fun getAreas(): List<AreaDto>
}
