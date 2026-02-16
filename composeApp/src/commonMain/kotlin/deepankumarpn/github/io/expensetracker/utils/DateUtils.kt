package deepankumarpn.github.io.expensetracker.utils

import deepankumarpn.github.io.expensetracker.domain.model.DurationFilter
import kotlinx.datetime.*

/**
 * Utility object for date-related operations.
 */
object DateUtils {

    /**
     * Get current date.
     */
    fun getCurrentDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    /**
     * Get current timestamp.
     */
    fun getCurrentInstant(): Instant {
        return Clock.System.now()
    }

    /**
     * Get start and end date for a duration filter.
     */
    fun getDateRange(
        filter: DurationFilter,
        year: Int? = null,
        month: Int? = null,
        customDays: Int? = null
    ): Pair<LocalDate, LocalDate> {
        val now = getCurrentDate()

        return when (filter) {
            DurationFilter.DAILY -> {
                now to now
            }

            DurationFilter.WEEKLY -> {
                val startOfWeek = now.minus(now.dayOfWeek.ordinal, DateTimeUnit.DAY)
                val endOfWeek = startOfWeek.plus(6, DateTimeUnit.DAY)
                startOfWeek to endOfWeek
            }

            DurationFilter.MONTHLY -> {
                val selectedYear = year ?: now.year
                val selectedMonth = month ?: now.monthNumber
                val startOfMonth = LocalDate(selectedYear, selectedMonth, 1)
                val endOfMonth = startOfMonth.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                startOfMonth to endOfMonth
            }

            DurationFilter.QUARTER_YEAR -> {
                val quarterStartMonth = ((now.monthNumber - 1) / 3) * 3 + 1
                val startOfQuarter = LocalDate(now.year, quarterStartMonth, 1)
                val endOfQuarter = startOfQuarter.plus(3, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                startOfQuarter to endOfQuarter
            }

            DurationFilter.HALF_YEAR -> {
                val halfStartMonth = if (now.monthNumber <= 6) 1 else 7
                val startOfHalf = LocalDate(now.year, halfStartMonth, 1)
                val endOfHalf = startOfHalf.plus(6, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                startOfHalf to endOfHalf
            }

            DurationFilter.CUSTOM -> {
                val days = customDays ?: 30
                val startDate = now.minus(days, DateTimeUnit.DAY)
                startDate to now
            }
        }
    }

    /**
     * Get display label for duration filter.
     */
    fun getDurationLabel(
        filter: DurationFilter,
        year: Int? = null,
        month: Int? = null,
        customDays: Int? = null
    ): String {
        val now = getCurrentDate()

        return when (filter) {
            DurationFilter.DAILY -> "Today"
            DurationFilter.WEEKLY -> "This Week"
            DurationFilter.MONTHLY -> {
                val selectedYear = year ?: now.year
                val selectedMonth = month ?: now.monthNumber
                "${Month(selectedMonth).name.lowercase().capitalize()} $selectedYear"
            }
            DurationFilter.QUARTER_YEAR -> "This Quarter"
            DurationFilter.HALF_YEAR -> "This Half Year"
            DurationFilter.CUSTOM -> "Last ${customDays ?: 30} Days"
        }
    }

    /**
     * Parse date string to LocalDate.
     */
    fun parseDate(dateString: String): LocalDate? {
        return try {
            LocalDate.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Parse ISO 8601 string to Instant.
     */
    fun parseInstant(instantString: String): Instant? {
        return try {
            Instant.parse(instantString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Format LocalDate to string.
     */
    fun formatDate(date: LocalDate): String {
        return date.toString()
    }

    /**
     * Format Instant to ISO 8601 string.
     */
    fun formatInstant(instant: Instant): String {
        return instant.toString()
    }

    /**
     * Get list of months.
     */
    fun getMonths(): List<String> {
        return Month.entries.map { it.name.lowercase().capitalize() }
    }

    /**
     * Get list of years (current year ± 5 years).
     */
    fun getYears(): List<Int> {
        val currentYear = getCurrentDate().year
        return (currentYear - 5..currentYear + 5).toList()
    }
}

private fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
