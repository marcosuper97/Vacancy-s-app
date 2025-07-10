package ru.practicum.android.diploma.data.db

import ru.practicum.android.diploma.domain.entity.Vacancy

object Converter {

    fun map(vacancyEntity: VacancyEntity): Vacancy {
        return Vacancy(
            id = vacancyEntity.id,
            url = vacancyEntity.url,
            name = vacancyEntity.name,
            currency = vacancyEntity.currency,
            salaryFrom = vacancyEntity.salaryFrom,
            salaryTo = vacancyEntity.salaryTo,
            employer = vacancyEntity.employer,
            employerLogo = vacancyEntity.employerLogo,
            area = vacancyEntity.area,
            experience = vacancyEntity.experience,
            workFormat = vacancyEntity.workFormat,
            employmentForm = vacancyEntity.employmentForm,
            description = vacancyEntity.description,
            city = vacancyEntity.city,
            street = vacancyEntity.street,
            building = vacancyEntity.building,
            addressDescription = vacancyEntity.addressDescription,
            keySkills = vacancyEntity.keySkills,
            additionTime = vacancyEntity.additionTime,
            isFavorite = true
        )
    }

    fun map(vacancy: Vacancy): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id,
            url = vacancy.url,
            name = vacancy.name,
            currency = vacancy.currency,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            employer = vacancy.employer,
            employerLogo = vacancy.employerLogo,
            area = vacancy.area,
            experience = vacancy.experience,
            workFormat = vacancy.workFormat,
            employmentForm = vacancy.employmentForm,
            description = vacancy.description,
            city = vacancy.city,
            street = vacancy.street,
            building = vacancy.building,
            addressDescription = vacancy.addressDescription,
            keySkills = vacancy.keySkills,
            additionTime = System.currentTimeMillis()
        )
    }
}
