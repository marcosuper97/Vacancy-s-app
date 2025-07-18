package ru.practicum.android.diploma.domain.interactors

import ru.practicum.android.diploma.domain.models.Areas

interface AreasInteractor {
    suspend fun getAreas():Result<Areas>
}
