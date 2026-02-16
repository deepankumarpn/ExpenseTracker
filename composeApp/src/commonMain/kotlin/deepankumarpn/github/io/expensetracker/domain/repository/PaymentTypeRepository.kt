package deepankumarpn.github.io.expensetracker.domain.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.PaymentType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for payment type operations.
 */
interface PaymentTypeRepository {
    /**
     * Get all payment types.
     */
    fun getPaymentTypes(): Flow<List<PaymentType>>

    /**
     * Add a new payment type.
     */
    suspend fun addPaymentType(name: String): StateFullResult<PaymentType>

    /**
     * Update an existing payment type.
     */
    suspend fun updatePaymentType(id: String, name: String): StateFullResult<Unit>

    /**
     * Delete a payment type.
     */
    suspend fun deletePaymentType(id: String): StateFullResult<Unit>

    /**
     * Check if a payment type is being used in any transactions.
     */
    suspend fun isPaymentTypeInUse(paymentTypeName: String): Boolean
}
