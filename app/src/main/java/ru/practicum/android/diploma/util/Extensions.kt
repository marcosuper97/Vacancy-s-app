package ru.practicum.android.diploma.util

import android.widget.TextView
import ru.practicum.android.diploma.R

fun MutableList<Any>.addLoadingItem() { // если список не содержит LoadingItem, то эта строка добавляет LoadingItem в конец списка.
    if (!this.any { it is LoadingItem }) {
        this.add(LoadingItem)
    }
}

fun MutableList<Any>.removeLoadingItem() { // удаляет из списка все элементы типа LoadingItem
    this.removeAll { it is LoadingItem }
}

fun TextView.setVacanciesCountText(count: Long) {
    this.text = String.format(
        context.getString(R.string.found),
        context.resources.getQuantityString(
            R.plurals.vacancies_count,
            count.toInt(),
            count
        )
    )
}

