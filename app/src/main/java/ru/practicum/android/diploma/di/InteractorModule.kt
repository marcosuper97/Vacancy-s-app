package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractor
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavoritesInteractorImpl
import ru.practicum.android.diploma.domain.interactors.FavoritesInteractor

val interactorModule = module {
    single<DetailsVacancyInteractor> {
        DetailsVacancyInteractorImpl(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}
