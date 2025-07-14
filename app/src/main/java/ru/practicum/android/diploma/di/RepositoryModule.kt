package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.searchvacancies.DetailsVacancyRepositoryImpl
import ru.practicum.android.diploma.data.searchvacancies.SearchVacanciesRepositoryImpl
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyRepository
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesRepository

val repositoryModule = module {
    single<SearchVacanciesRepository> {
        SearchVacanciesRepositoryImpl(get())
    }

    single<DetailsVacancyRepository> {
        DetailsVacancyRepositoryImpl(get())
    }
}
