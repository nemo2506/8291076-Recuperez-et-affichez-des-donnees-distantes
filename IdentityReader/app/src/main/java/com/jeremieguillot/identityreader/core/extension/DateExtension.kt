package com.jeremieguillot.identityreader.core.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toLocaleDateString(): String {
    val formatter = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toLocaleDateStringSeparated(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toMRZFormat(): String {
    val outputFormat = SimpleDateFormat("yyMMdd", Locale.getDefault())
    return outputFormat.format(this)
}

fun String.fromYYMMDDtoDate(): Date? {
    if (isBlank()) return null
    val sdf = SimpleDateFormat("yyMMdd", Locale.getDefault())
    return sdf.parse(this) ?: Date()
}

fun String.fromDDMMYYYYtoDate(): Date {
    if (isBlank()) return Date()
    val sdf = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    return sdf.parse(this) ?: Date()
}