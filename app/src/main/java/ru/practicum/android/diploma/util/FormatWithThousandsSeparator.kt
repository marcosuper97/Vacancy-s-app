package ru.practicum.android.diploma.util

fun Int?.formatWithThousandsSeparator(): String {
    return this.let { String.format("%,d", it).replace(',', ' ') } ?: ""
}
