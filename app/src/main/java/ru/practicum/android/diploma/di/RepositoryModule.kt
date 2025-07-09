package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.domain.repositories.FavoritesRepository

val repositoryModule = module {
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get())
    }
}
