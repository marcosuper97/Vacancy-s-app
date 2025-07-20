package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractor
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractorImpl
import ru.practicum.android.diploma.domain.impl.AreasInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavoritesInteractorImpl
import ru.practicum.android.diploma.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.interactors.AreasInteractor
import ru.practicum.android.diploma.domain.interactors.FavoritesInteractor
import ru.practicum.android.diploma.domain.interactors.IndustryInteractor
import ru.practicum.android.diploma.domain.repositories.SearchVacanciesInteractor

val interactorModule = module {

    factory<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
    factory<DetailsVacancyInteractor> {
        DetailsVacancyInteractorImpl(get(), get())
    }
    factory<SearchVacanciesInteractor> { SearchVacanciesInteractorImpl(get()) }

    single<IndustryInteractor> {
        IndustryInteractorImpl(get())
    }

    single<AreasInteractor> {
        AreasInteractorImpl(get())
    }
}
