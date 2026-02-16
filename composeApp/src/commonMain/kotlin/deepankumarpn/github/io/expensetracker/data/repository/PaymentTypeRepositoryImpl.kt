package deepankumarpn.github.io.expensetracker.data.repository

import deepankumarpn.github.io.expensetracker.base.StateFullResult
import deepankumarpn.github.io.expensetracker.domain.model.PaymentType
import deepankumarpn.github.io.expensetracker.domain.repository.PaymentTypeRepository
import deepankumarpn.github.io.expensetracker.utils.generateUUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock

/**
 * In-memory implementation of PaymentTypeRepository.
 */
class PaymentTypeRepositoryImpl : PaymentTypeRepository {
    private val paymentTypes = MutableStateFlow(
        listOf(
            PaymentType("1", "Cash", true, Clock.System.now()),
            PaymentType("2", "Card", true, Clock.System.now()),
            PaymentType("3", "UPI", false, Clock.System.now()),
            PaymentType("4", "Net Banking", false, Clock.System.now())
        )
    )

    override fun getPaymentTypes(): Flow<List<PaymentType>> = paymentTypes

    override suspend fun addPaymentType(name: String): StateFullResult<PaymentType> {
        return try {
            val newPaymentType = PaymentType(
                id = generateUUID(),
                name = name,
                isDefault = false,
                createdAt = Clock.System.now()
            )
            paymentTypes.value = paymentTypes.value + newPaymentType
            StateFullResult.Success(newPaymentType)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to add payment type")
        }
    }

    override suspend fun updatePaymentType(id: String, name: String): StateFullResult<Unit> {
        return try {
            paymentTypes.value = paymentTypes.value.map {
                if (it.id == id) it.copy(name = name) else it
            }
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to update payment type")
        }
    }

    override suspend fun deletePaymentType(id: String): StateFullResult<Unit> {
        return try {
            paymentTypes.value = paymentTypes.value.filter { it.id != id }
            StateFullResult.Success(Unit)
        } catch (e: Exception) {
            StateFullResult.Error(e.message ?: "Failed to delete payment type")
        }
    }

    override suspend fun isPaymentTypeInUse(paymentTypeName: String): Boolean {
        // In a real implementation, this would check the transaction repository
        return false
    }
}
