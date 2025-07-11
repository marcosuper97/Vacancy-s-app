package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.search.VacancySearchResponseDto

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun vacanciesSearchRequest(requestQuery: Map<String, String>): Result<VacancySearchResponseDto>
}
