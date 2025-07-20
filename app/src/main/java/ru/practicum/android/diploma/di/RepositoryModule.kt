package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.AreasRepositoryImpl
import ru.practicum.android.diploma.data.impl.DetailsVacancyRepositoryImpl
import ru.practicum.android.diploma.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.data.impl.IndustryRepositoryImpl
import ru.practicum.android.diploma.data.impl.FiltersRepositoryImpl
import ru.practicum.android.diploma.data.impl.SearchVacanciesRepositoryImpl
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyRepository
import ru.practicum.android.diploma.domain.repositories.AreasRepository
import ru.practicum.android.diploma.domain.repositories.FavoritesRepository
import ru.practicum.android.diploma.domain.repositories.FiltersRepository
import ru.practicum.android.diploma.domain.repositories.IndustryRepository
import ru.practicum.android.diploma.domain.repositories.SearchVacanciesRepository

private val ACCESS_AREAS: Set<String> = setOf(
    "Россия",
    "Украина",
    "Казахстан",
    "Азербайджан",
    "Беларусь",
    "Грузия",
    "Кыргызстан",
    "Узбекистан",
    "Другие регионы"
)

val repositoryModule = module {

    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(get())
    }

    factory<SearchVacanciesRepository> {
        SearchVacanciesRepositoryImpl(get(), get())
    }

    factory<DetailsVacancyRepository> {
        DetailsVacancyRepositoryImpl(get())
    }

    single<IndustryRepository> {
        IndustryRepositoryImpl(get())
    }

    single<AreasRepository> {
        AreasRepositoryImpl(get(), ACCESS_AREAS)
    }

    factory<FiltersRepository> {
        FiltersRepositoryImpl(get())
    }
}
