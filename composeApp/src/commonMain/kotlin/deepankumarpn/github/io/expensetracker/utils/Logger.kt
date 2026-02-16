package deepankumarpn.github.io.expensetracker.utils

/**
 * Simple logger utility for the application.
 */
object Logger {

    private const val TAG = "ExpenseTracker"

    fun d(message: String, tag: String = TAG) {
        println("DEBUG/$tag: $message")
    }

    fun i(message: String, tag: String = TAG) {
        println("INFO/$tag: $message")
    }

    fun w(message: String, tag: String = TAG) {
        println("WARN/$tag: $message")
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = TAG) {
        println("ERROR/$tag: $message")
        throwable?.printStackTrace()
    }
}
