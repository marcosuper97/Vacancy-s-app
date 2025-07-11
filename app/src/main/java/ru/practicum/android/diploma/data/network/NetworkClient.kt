package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.search.VacancySearchResponseDto

interface NetworkClient {
    suspend fun doRequest(dto: Any): Result<VacancyDetailsResponse>
    suspend fun vacanciesSearchRequest(requestQuery: Map<String, String>): Result<VacancySearchResponseDto>
}
