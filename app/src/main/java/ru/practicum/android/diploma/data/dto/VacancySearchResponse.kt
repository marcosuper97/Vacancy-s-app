package ru.practicum.android.diploma.data.dto


data class VacancySearchResponse (
    val resultCount:Int,
    val found:Int,
    val perPage:Int,
    val pages:Int,
    val page: Int,
    val items:List<VacancyDto>
) : Response()


