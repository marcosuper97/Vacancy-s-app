package ru.practicum.android.diploma.data.db

import ru.practicum.android.diploma.data.dto.Area
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.LogoUrlDto
import ru.practicum.android.diploma.data.dto.SalaryRangeDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.WorkFormat

object Converter {
    fun map(vacancy: VacancyEntity): VacancyDto {
        return VacancyDto(
            id = vacancy.id,
            name = vacancy.name,
            employer = EmployerDto(logoUrls = LogoUrlDto(size90 = vacancy.employerLogo), name = vacancy.name),
            area = Area(id = null, name = vacancy.area),
            salaryRange = SalaryRangeDto(
                currency = vacancy.currency,
                from = vacancy.salaryFrom,
                to = vacancy.salaryTo,
                gross = null
            ),
            workFormat = WorkFormat(name = vacancy.workFormat)
        )
    }
}
