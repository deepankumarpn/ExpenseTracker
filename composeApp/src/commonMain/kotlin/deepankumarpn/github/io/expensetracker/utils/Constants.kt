package deepankumarpn.github.io.expensetracker.utils

/**
 * Application-wide constants.
 */
object Constants {
    const val APP_NAME = "ExpenseTracker"
    const val DATE_FORMAT_PATTERN = "yyyy-MM-dd"
    const val DATETIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    // DataStore keys
    const val DATASTORE_NAME = "expense_tracker_prefs"
    const val KEY_SPREADSHEET_ID = "spreadsheet_id"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_IS_AUTHENTICATED = "is_authenticated"

    // Validation
    const val MAX_AMOUNT_DIGITS = 10
    const val MAX_NOTE_LENGTH = 500
    const val MAX_CATEGORY_LENGTH = 50
    const val MAX_PAYMENT_TYPE_LENGTH = 50
}
