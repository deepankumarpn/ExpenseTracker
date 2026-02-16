package deepankumarpn.github.io.expensetracker.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

/**
 * Domain model representing a financial transaction.
 */
data class Transaction(
    val id: String,
    val type: TransactionType,
    val amount: Double,
    val category: String,
    val paymentType: String,
    val note: String,
    val date: LocalDate,
    val createdAt: Instant
)
