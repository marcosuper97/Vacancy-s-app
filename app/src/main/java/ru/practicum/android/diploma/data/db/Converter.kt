package ru.practicum.android.diploma.data.db

import ru.practicum.android.diploma.data.dto.AddressDto
import ru.practicum.android.diploma.data.dto.Area
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.EmploymentFormDto
import ru.practicum.android.diploma.data.dto.ExperienceDto
import ru.practicum.android.diploma.data.dto.LogoUrlDto
import ru.practicum.android.diploma.data.dto.SalaryRangeDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.WorkFormatDto

object Converter {
    fun map(vacancy: VacancyEntity): VacancyDto {
        return VacancyDto(
            id = vacancy.id,
            name = vacancy.name,
            employer = EmployerDto(logoUrls = LogoUrlDto(size90 = vacancy.employerLogo), name = vacancy.name),
            area = Area(name = vacancy.area),
            salaryRange = SalaryRangeDto(
                currency = vacancy.currency,
                from = vacancy.salaryFrom,
                to = vacancy.salaryTo,
                gross = null
            ),
            workFormat = WorkFormatDto(name = vacancy.workFormat),
            experience = ExperienceDto(name = vacancy.experience),
            employmentFrom = EmploymentFormDto(name = vacancy.employmentForm),
            description = vacancy.description,
            address = AddressDto(city = vacancy.city, street = vacancy.street, building = vacancy.building, description = vacancy.addressDescription),
            keySkills = vacancy.keySkills
        )
    }
}
