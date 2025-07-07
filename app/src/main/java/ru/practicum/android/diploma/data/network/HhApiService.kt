package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface HhApiService {
    @GET("vacancies")
    suspend fun search(
        @Query("term") text: String,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("area") area: String?,
        @Query("industry") industry: String?,
    ): VacancySearchResponse

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsResponse
}
