package ru.practicum.android.diploma.data.dto.vacancy.vacancydetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancyDetailsDto(
    val id: String,
    val name: String,
    val employer: EmployerDto,
    val area: VacancyAreaDto,
    @SerialName("salary_range")
    val salaryRange: SalaryRangeDto?,
    @SerialName("experience")
    val experience: ExperienceDto,
    @SerialName("schedule")
    val schedule: ScheduleDto,
    @SerialName("employment")
    val employment: EmploymentDto,
    val description: String,
    @SerialName("key_skills")
    val keySkills: List<KeySkillDto>
)

@Serializable
data class EmployerDto(
    @SerialName("logo_urls")
    val logoUrls: LogoUrlDto,
    val name: String,
)

@Serializable
data class LogoUrlDto(
    @SerialName("90")
    val size90: String,
)

@Serializable
data class VacancyAreaDto(
    val id: String,
    val name: String,
)

@Serializable
data class SalaryRangeDto(
    val currency: CurrencyDto,
    val from: Int? = 0,
    val gross: Boolean?,
    val to: Int? = 0
)

@Serializable
enum class CurrencyDto {
    @SerialName("USD")
    USD,

    @SerialName("EUR")
    EUR,

    @SerialName("RUR")
    RUB,

    @SerialName("AZN")
    AZN,

    @SerialName("BYR")
    BYR(),

    @SerialName("GEL")
    GEL,

    @SerialName("KGS")
    KGS,

    @SerialName("KZT")
    KZT,

    @SerialName("UAH")
    UAH,

    @SerialName("UZS")
    UZS
}

@Serializable
data class ExperienceDto(
    val name: String
)

@Serializable
data class ScheduleDto(
    val name: String
)

@Serializable
data class EmploymentDto(
    val name: String
)

@Serializable
data class KeySkillDto(
    val name: String
)
