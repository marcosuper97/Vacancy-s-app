package ru.practicum.android.diploma.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
