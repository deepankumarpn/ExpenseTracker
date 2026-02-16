package deepankumarpn.github.io.expensetracker.utils

import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import kotlinx.datetime.*

/**
 * Extension functions for the application.
 */

// String extensions
fun String.isValidEmail(): Boolean {
    return this.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
}

// Double extensions
fun Double.formatAmount(currency: CurrencyType): String {
    return "${currency.symbol}${String.format("%.2f", this)}"
}

fun Double.formatAmountWithCommas(currency: CurrencyType): String {
    val formatted = String.format("%,.2f", this)
    return "${currency.symbol}$formatted"
}

// LocalDate extensions
fun LocalDate.toDisplayString(): String {
    val monthShort = this.month.name.lowercase().capitalize().take(3)
    return "${this.dayOfMonth} $monthShort ${this.year}"
}

fun LocalDate.isToday(): Boolean {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return this == now
}

fun LocalDate.isPast(): Boolean {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return this < now
}

fun LocalDate.isFuture(): Boolean {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return this > now
}

// Instant extensions
fun Instant.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return this.toLocalDateTime(timeZone).date
}

fun Instant.toFormattedString(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val localDateTime = this.toLocalDateTime(timeZone)
    return "${localDateTime.date.toDisplayString()} at ${localDateTime.hour}:${localDateTime.minute.toString().padStart(2, '0')}"
}

// UUID generation
@OptIn(kotlin.uuid.ExperimentalUuidApi::class)
fun generateUUID(): String {
    return kotlin.uuid.Uuid.random().toString()
}

// String capitalize extension (for older Kotlin versions compatibility)
private fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
