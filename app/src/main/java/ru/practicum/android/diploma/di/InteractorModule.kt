package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractor
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavoritesInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.interactors.FavoritesInteractor
import ru.practicum.android.diploma.domain.searchvacancies.SearchVacanciesInteractor

val interactorModule = module {

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
    single<DetailsVacancyInteractor> {
        DetailsVacancyInteractorImpl(get())
    }
    factory<SearchVacanciesInteractor> { SearchVacanciesInteractorImpl(get()) }
}
