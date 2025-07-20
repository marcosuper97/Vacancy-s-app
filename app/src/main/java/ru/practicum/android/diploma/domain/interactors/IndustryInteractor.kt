package ru.practicum.android.diploma.domain.interactors

import ru.practicum.android.diploma.domain.models.Industry

interface IndustryInteractor {
    suspend fun getIndustries(): Result<List<Industry>>
}
