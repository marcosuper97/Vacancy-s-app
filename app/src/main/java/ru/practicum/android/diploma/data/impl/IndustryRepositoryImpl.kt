package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repositories.IndustryRepository

class IndustryRepositoryImpl():IndustryRepository {
    override suspend fun getIndustries(): Result<List<Industry>> {
        TODO("Not yet implemented")
    }
}
