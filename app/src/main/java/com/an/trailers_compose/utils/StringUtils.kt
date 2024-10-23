package com.an.trailers_compose.utils

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

private fun String.getDate(): Date? {
    val pos = ParsePosition(0)
    val currentDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return currentDateFormat.parse(this, pos)
}
fun String.getFormattedDate(): String {
    val cal = Calendar.getInstance()
    return this.getDate()?.let {
         cal.time = it
         val day = cal.get(Calendar.DATE)
         when (day % 10) {
             1 -> SimpleDateFormat("MMMM d'st', yyyy").format(it)
             2 -> SimpleDateFormat("MMMM d'nd', yyyy").format(it)
             3 -> SimpleDateFormat("MMMM d'rd', yyyy").format(it)
             else -> SimpleDateFormat("MMMM d'th', yyyy").format(it)
         }
     } ?: ""
}