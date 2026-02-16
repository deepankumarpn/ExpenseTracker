package deepankumarpn.github.io.expensetracker.domain.model

import kotlinx.datetime.Instant

/**
 * Domain model representing a payment type.
 */
data class PaymentType(
    val id: String,
    val name: String,
    val isDefault: Boolean,
    val createdAt: Instant
)
