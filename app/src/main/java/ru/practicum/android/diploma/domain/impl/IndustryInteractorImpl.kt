package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.interactors.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repositories.IndustryRepository

class IndustryInteractorImpl(
    private val industryRepository: IndustryRepository
) : IndustryInteractor {
    override suspend fun getIndustries(): Result<List<Industry>> = industryRepository.getIndustries()
}
