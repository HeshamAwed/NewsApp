package com.hesham.newsapp.ui.kts

import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.to12HourFormat(
    fromFormat: String = "HH:mm:ss",
    toFormat: String = "hh:mm a"
): String {
    return try {
        val dateTimeFromFormat = DateTimeFormat.forPattern(fromFormat)
        val dateTimeToFormat = DateTimeFormat.forPattern(toFormat)
        val time = dateTimeFromFormat.parseDateTime(this)
        time.toString(dateTimeToFormat)
    } catch (exception: Exception) {
        ""
    }
}

private const val originalDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val toShortTime = "hh:mm a"
const val displayDateAndTime = "dd MMM hh:mm a"
const val readableDateAndTime = "dd MMM yyyy"
const val shortDateAndTime = "dd MMM"
const val shortDayMonthDateAndTime = "dd MMM"

internal var appTimeZone = "Africa/Cairo"
    private set

fun String.formatShortTime(): String {
    val simpleDateFormat = getDateSimpleFormatter()
    return simpleDateFormat.parse(this)?.time.let { getShortTimeSimpleDateFormatter().format(it) }
        .orEmpty()
}

fun String.formatTime(): String {
    return try {
        val simpleDateFormat = getShortTime()
        simpleDateFormat.parse(this)?.time.let { toShortTime().format(it) }.orEmpty()
    } catch (ex: Exception) {
        ex.printStackTrace()
        ""
    }
}

fun String.formatToDisplayDateAndTime(): String {
    val originalFormat = getDateSimpleFormatter()
    return originalFormat.parse(this)?.time.let { getDisplaySimpleDateTimeFormatter().format(it) }
        .orEmpty()
}

fun String.formatToReadableDate(): String {
    val originalFormat = getDateSimpleFormatter()
    return originalFormat.parse(this)?.time.let { getReadableSimpleDateTimeFormatter().format(it) }
        .orEmpty()
}

fun String.shortDate(): String {
    val originalFormat = getDateSimpleFormatter()
    return originalFormat.parse(this)?.time.let { getReadableSimpleDateTimeFormatter().format(it) }
        .orEmpty()
}

fun String.formatToShortDayAndMonthDate(): String = getDateSimpleFormatter().parse(this)?.time.let {
    getShortDayAndMonthSimpleDateTimeFormatter().format(
        it
    )
}.orEmpty()

fun getShortDayAndMonthSimpleDateTimeFormatter(): SimpleDateFormat = SimpleDateFormat(
    shortDayMonthDateAndTime,
    Locale.getDefault()
).apply {
    timeZone = TimeZone.getTimeZone(appTimeZone)
}

fun getReadableSimpleDateTimeFormatter(): SimpleDateFormat = SimpleDateFormat(
    readableDateAndTime,
    Locale.getDefault()
).apply {
    timeZone = TimeZone.getTimeZone(appTimeZone)
}

fun getShortSimpleDateTimeFormatter(): SimpleDateFormat = SimpleDateFormat(
    readableDateAndTime,
    Locale.getDefault()
).apply {
    timeZone = TimeZone.getTimeZone(appTimeZone)
}

fun getDisplaySimpleDateTimeFormatter(): SimpleDateFormat = SimpleDateFormat(
    displayDateAndTime,
    Locale.getDefault()
).apply {
    timeZone = TimeZone.getTimeZone(appTimeZone)
}

fun getDateSimpleFormatter(): SimpleDateFormat {
    return SimpleDateFormat(originalDateTimeFormat, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone(appTimeZone)
    }
}

fun getShortTimeSimpleDateFormatter(): SimpleDateFormat = SimpleDateFormat(
    toShortTime,
    Locale.getDefault()
).apply {
    timeZone = TimeZone.getTimeZone(appTimeZone)
}

fun getShortTime(): SimpleDateFormat {
    return SimpleDateFormat(toShortTime, Locale.US).apply {
        timeZone = TimeZone.getTimeZone(appTimeZone)
    }
}

fun toShortTime(): SimpleDateFormat {
    return SimpleDateFormat(toShortTime, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone(appTimeZone)
    }
}