package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.interactors.FavoritesInteractor
import ru.practicum.android.diploma.domain.interactors.FavoritesInteractorImpl

val interactorModule = module {

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}
