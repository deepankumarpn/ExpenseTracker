package deepankumarpn.github.io.expensetracker.domain.model

/**
 * Configuration for Google Sheets structure.
 */
object SheetConfig {
    const val SPREADSHEET_NAME = "ExpenseTracker_Data"

    object Sheets {
        const val TRANSACTIONS = "transactions"
        const val CATEGORIES = "categories"
        const val PAYMENT_TYPES = "payment_types"
        const val SETTINGS = "settings"
        const val BALANCE_RUNNING = "balance_running"
    }

    object DefaultCategories {
        val ALL = listOf("Food", "Travel", "Grocery", "Daily Utility Bills")
    }

    object DefaultPaymentTypes {
        val ALL = listOf("Cash", "Net Banking", "UPI", "Card")
    }

    object DefaultSettings {
        const val KEY_CURRENCY = "currency"
        const val KEY_HOME_DURATION = "homeDuration"
        const val KEY_CUSTOM_DAYS = "customDays"
        const val KEY_CUSTOM_SUMMARY_ITEMS = "customSummaryItems"

        val ALL = mapOf(
            KEY_CURRENCY to "INR",
            KEY_HOME_DURATION to "MONTHLY",
            KEY_CUSTOM_DAYS to "30",
            KEY_CUSTOM_SUMMARY_ITEMS to "[]"
        )
    }
}
