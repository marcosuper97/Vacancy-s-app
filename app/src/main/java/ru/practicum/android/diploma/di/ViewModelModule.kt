package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import ru.practicum.android.diploma.presentation.main.MainViewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailsViewModel

val viewModelModule = module {
    viewModel<MainViewModel> { MainViewModel(get()) }
    viewModel {
        VacancyDetailsViewModel(get(), get())
    }
}
