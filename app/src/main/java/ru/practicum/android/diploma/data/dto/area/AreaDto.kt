package ru.practicum.android.diploma.data.dto.area

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AreaDto(
    val id: String,
    @SerialName("parent_id")
    val parentId: String?,
    val name: String,
    val areas: List<AreaDto>
)

