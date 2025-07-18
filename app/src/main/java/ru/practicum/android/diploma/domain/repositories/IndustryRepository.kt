package ru.practicum.android.diploma.domain.repositories

import ru.practicum.android.diploma.domain.models.Industry

interface IndustryRepository {
    suspend fun getIndustries(): Result<List<Industry>>
}
