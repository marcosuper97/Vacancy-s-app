package ru.practicum.android.diploma.domain.models

data class Areas(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<Areas>
)
