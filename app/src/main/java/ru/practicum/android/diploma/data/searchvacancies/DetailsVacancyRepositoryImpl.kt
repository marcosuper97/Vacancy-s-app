package ru.practicum.android.diploma.data.searchvacancies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.vacancy.vacancydetails.CurrencyDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancydetails.EmployerDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancydetails.SalaryRangeDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancydetails.VacancyAreaDto
import ru.practicum.android.diploma.data.dto.vacancy.vacancydetails.VacancyDetailsDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Currency
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.SalaryRange
import ru.practicum.android.diploma.domain.models.VacancyDetails

private fun VacancyDetailsDto.toVacancyDetails() = VacancyDetails(
    id = id,
    name = name,
    employer = employer.toEmployer(),
    area = area.toArea(),
    salaryRange = salaryRange.toSalaryRange(),
    experience = experience.name,
    schedule = schedule.name,
    employment = employment.name,
    description = description,
    keySkills = keySkills
)

private fun EmployerDto.toEmployer() = Employer(
    logoUrl = logoUrls.size90,
    name = name
)

private fun VacancyAreaDto.toArea() = Area(
    id = id,
    name = name
)

private fun SalaryRangeDto.toSalaryRange() = SalaryRange(
    currency = currency.toCurrency(),
    from = from,
    gross = gross,
    to = to
)

fun CurrencyDto.toCurrency(): Currency {
    return when (this) {
        CurrencyDto.USD -> Currency.USD
        CurrencyDto.EUR -> Currency.EUR
        CurrencyDto.RUB -> Currency.RUB
        CurrencyDto.AZN -> Currency.AZN
        CurrencyDto.BYR -> Currency.BYR
        CurrencyDto.GEL -> Currency.GEL
        CurrencyDto.KGS -> Currency.KGS
        CurrencyDto.KZT -> Currency.KZT
        CurrencyDto.UAH -> Currency.UAH
        CurrencyDto.UZS -> Currency.UZS
    }
}

class DetailsVacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : DetailsVacancyRepository {
    override fun doRequest(vacancyId: String): Flow<Result<VacancyDetails>> = flow {
        val response = withContext(Dispatchers.IO) {
            networkClient.detailsVacancyRequest(vacancyId)
        }

        response
            .onSuccess { data ->
                emit(Result.success(data.toVacancyDetails()))
            }
            .onFailure { error ->
                emit(Result.failure(error))
            }
    }
}
