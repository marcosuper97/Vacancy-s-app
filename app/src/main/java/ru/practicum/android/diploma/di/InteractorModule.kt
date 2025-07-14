package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractor
import ru.practicum.android.diploma.domain.detailsvacancy.DetailsVacancyInteractorImpl

val interactorModule = module {
    single<DetailsVacancyInteractor> {
        DetailsVacancyInteractorImpl(get())
    }
}
