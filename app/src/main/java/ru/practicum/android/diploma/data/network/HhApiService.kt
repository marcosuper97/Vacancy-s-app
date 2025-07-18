package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.area.AreaDto
import ru.practicum.android.diploma.data.dto.industry.IndustryGroupDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancydetails.VacancyDetailsDto
import ru.practicum.android.diploma.data.dto.vacancy.vacanysearch.VacancySearchResponseDto

interface HhApiService {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Vacancy's app/1.0 anannat@yandex.ru"
    )
    @GET("vacancies")
    suspend fun searchVacancies(
        @QueryMap options: Map<String, String>
    ): VacancySearchResponseDto

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Vacancy's app/1.0 anannat@yandex.ru"
    )
    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsDto

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Vacancy's app/1.0 anannat@yandex.ru"
    )
    @GET("industries")
    suspend fun getIndustries(): List<IndustryGroupDto>

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Vacancy's app/1.0 anannat@yandex.ru"
    )
    @GET("areas")
    suspend fun getAreas(): List<AreaDto>
}
