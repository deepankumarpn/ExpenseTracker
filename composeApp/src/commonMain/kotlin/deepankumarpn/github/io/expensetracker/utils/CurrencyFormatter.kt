package deepankumarpn.github.io.expensetracker.utils

import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType

/**
 * Utility object for formatting currency values.
 */
object CurrencyFormatter {

    /**
     * Format amount with currency symbol.
     */
    fun format(amount: Double, currency: CurrencyType): String {
        return amount.formatAmountWithCommas(currency)
    }

    /**
     * Format amount without currency symbol.
     */
    fun formatWithoutSymbol(amount: Double): String {
        return String.format("%,.2f", amount)
    }

    /**
     * Parse amount string to double.
     */
    fun parseAmount(amountString: String): Double? {
        return try {
            amountString.replace(",", "").toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Validate amount string.
     */
    fun isValidAmount(amountString: String): Boolean {
        val cleaned = amountString.replace(",", "")
        return cleaned.matches(Regex("^\\d+(\\.\\d{1,2})?$"))
    }
}
