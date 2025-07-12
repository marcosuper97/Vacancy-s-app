package ru.practicum.android.diploma.domain.models

data class VacancyDetails(
    val id: String,
    val name: String,
    val employer: Employer,
    val area: Area,
    val salaryRange: SalaryRange,
    val experience: String,
    val schedule: String,
    val employment: String,
    val description: String,
    val keySkills: List<String>
)

data class Employer(
    val logoUrl: String,
    val name: String,
)

data class Area(
    val id: String,
    val name: String,
)

data class SalaryRange(
    val currency: Currency,
    val from: Int? = 0,
    val gross: Boolean,
    val to: Int? = 0
)

enum class Currency(val label: String) {
    USD("$"),
    EUR("€"),
    RUB("₽"),
    AZN("₼"),
    BYR("br"),
    GEL("₾"),
    KGS("с"),
    KZT("₸"),
    UAH("₴"),
    UZS("so\'m")
}
