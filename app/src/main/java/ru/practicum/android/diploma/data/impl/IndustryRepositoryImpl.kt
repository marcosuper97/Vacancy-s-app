package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repositories.IndustryRepository
import ru.practicum.android.diploma.util.toModel

class IndustryRepositoryImpl(private val networkClient: NetworkClient) : IndustryRepository {
    override suspend fun getIndustries(): Result<List<Industry>> {
        return networkClient.getIndustries()
            .map { dto ->
                dto.flatMap { industriesGroup -> industriesGroup.industries }
                    .map { industryDto -> industryDto.toModel() }
            }
    }
}
