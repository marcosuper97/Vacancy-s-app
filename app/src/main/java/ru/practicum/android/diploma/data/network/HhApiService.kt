package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
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
        @Query("only_with_salary") onlyWithSalary: Boolean?
    ): VacancySearchResponse

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Path("vacancy_id") vacancyId: String
    ): VacancyDetailsResponse

    @GET("industries")
    suspend fun getIndustries(): List<IndustryDto>

    @GET("areas")
    suspend fun getAreas(): List<AreaDto>
}
