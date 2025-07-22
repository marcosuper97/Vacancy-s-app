package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.country.CountryViewModel
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.main.MainViewModel
import ru.practicum.android.diploma.presentation.placework.PlaceWorkViewModel
import ru.practicum.android.diploma.presentation.region.RegionViewModel
import ru.practicum.android.diploma.presentation.sectorwork.SectorWorkViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailsViewModel

val viewModelModule = module {
    viewModel<MainViewModel> { MainViewModel(get()) }
    viewModel {
        VacancyDetailsViewModel(get(), get(), get())
    }
    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        PlaceWorkViewModel(get())
    }

    viewModel {
        CountryViewModel(get())
    }

    viewModel {
        RegionViewModel(get())
    }

    viewModel {
        SectorWorkViewModel(get())
    }
}
