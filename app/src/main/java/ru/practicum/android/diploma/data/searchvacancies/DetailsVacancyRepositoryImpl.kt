package ru.practicum.android.diploma.data.searchvacancies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.vacancy.vacancydetails.VacancyDetailsDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.toCurrencySymbol

class DetailsVacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : DetailsVacancyRepository {
    override suspend fun doRequest(vacancyId: String): Result<VacancyDetails> {
        val response = withContext(Dispatchers.IO) {
            networkClient.detailsVacancyRequest(vacancyId)
        }

        response
            .onSuccess { data ->
                return Result.success(mapResponse(data))
            }
            .onFailure { error ->
                emit(Result.failure(error))
            }
    }

    private fun mapResponse(dto: VacancyDetailsDto): VacancyDetails {
        return VacancyDetails(
            vacancyId = dto.id,
            vacancyName = dto.name,
            employerName = dto.employer?.name,
            employerLogo = dto.employer?.employerLogo?.path,
            address = when (dto.address?.raw) {
                null -> dto.area.name
                else -> dto.address.raw
            },
            salaryFrom = dto.salary?.from.toString(),
            salaryTo = dto.salary?.to.toString(),
            currency = dto.salary?.currency.toCurrencySymbol(),
            workFormat = dto.workFormat?.map { it.name },
            experience = dto.experience?.name,
            linkUrl = dto.linkUrl,
            description = dto.description,
            keySkills = dto.keySkills.map { it.name }
        )
    }
}
