package deepankumarpn.github.io.expensetracker.presentation.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import deepankumarpn.github.io.expensetracker.domain.model.*
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.*
import deepankumarpn.github.io.expensetracker.utils.formatAmountWithCommas
import deepankumarpn.github.io.expensetracker.utils.toDisplayString
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.ui.tooling.preview.Preview

/**
 * History screen composable.
 */
@Composable
fun HistoryScreen(
    state: HistoryContract.State,
    onEvent: (HistoryContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
            // Filter Section
            FilterSection(
                selectedDuration = state.selectedDuration,
                onDurationChanged = { onEvent(HistoryContract.Event.DurationFilterChanged(it)) }
            )

            // Summary Card
            SummaryCard(
                totalIncome = state.totalIncome,
                totalExpense = state.totalExpense,
                currency = state.currency
            )

            // Transactions List
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.transactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "No transactions found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.transactions) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            currency = state.currency
                        )
                    }
                }
            }
        }
    }

@Composable
fun FilterSection(
    selectedDuration: DurationFilter,
    onDurationChanged: (DurationFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Filter by Duration",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedDuration == DurationFilter.DAILY,
                    onClick = { onDurationChanged(DurationFilter.DAILY) },
                    label = { Text("Daily") }
                )
                FilterChip(
                    selected = selectedDuration == DurationFilter.WEEKLY,
                    onClick = { onDurationChanged(DurationFilter.WEEKLY) },
                    label = { Text("Weekly") }
                )
                FilterChip(
                    selected = selectedDuration == DurationFilter.MONTHLY,
                    onClick = { onDurationChanged(DurationFilter.MONTHLY) },
                    label = { Text("Monthly") }
                )
            }
        }
    }
}

@Composable
fun SummaryCard(
    totalIncome: Double,
    totalExpense: Double,
    currency: CurrencyType,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Income Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = IncomeGreenLight
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Income",
                        tint = IncomeGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Income",
                        style = MaterialTheme.typography.bodySmall,
                        color = IncomeGreen
                    )
                }
                Text(
                    text = totalIncome.formatAmountWithCommas(currency),
                    style = MaterialTheme.typography.titleLarge,
                    color = IncomeGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Expense Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = ExpenseRedLight
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Expense",
                        tint = ExpenseRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Expense",
                        style = MaterialTheme.typography.bodySmall,
                        color = ExpenseRed
                    )
                }
                Text(
                    text = totalExpense.formatAmountWithCommas(currency),
                    style = MaterialTheme.typography.titleLarge,
                    color = ExpenseRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    currency: CurrencyType,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (transaction.type == TransactionType.INCOME) IncomeGreenLight else ExpenseRedLight
    val iconColor = if (transaction.type == TransactionType.INCOME) IncomeGreen else ExpenseRed

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icon with background
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (transaction.type == TransactionType.INCOME)
                            Icons.Default.ArrowDownward
                        else
                            Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Details
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = transaction.category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${transaction.paymentType} • ${transaction.date.toDisplayString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    if (transaction.note.isNotBlank()) {
                        Text(
                            text = transaction.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Amount
            Text(
                text = transaction.amount.formatAmountWithCommas(currency),
                style = MaterialTheme.typography.titleLarge,
                color = iconColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    ExpenseTrackerTheme {
        HistoryScreen(
            state = HistoryContract.State(
                transactions = listOf(
                    Transaction(
                        id = "1",
                        type = TransactionType.EXPENSE,
                        amount = 500.0,
                        category = "Food",
                        paymentType = "UPI",
                        note = "Grocery shopping",
                        date = currentDate,
                        createdAt = Clock.System.now()
                    ),
                    Transaction(
                        id = "2",
                        type = TransactionType.INCOME,
                        amount = 5000.0,
                        category = "Salary",
                        paymentType = "Net Banking",
                        note = "Monthly salary",
                        date = currentDate,
                        createdAt = Clock.System.now()
                    ),
                    Transaction(
                        id = "3",
                        type = TransactionType.EXPENSE,
                        amount = 1200.0,
                        category = "Travel",
                        paymentType = "Card",
                        note = "Cab fare",
                        date = currentDate,
                        createdAt = Clock.System.now()
                    )
                ),
                totalIncome = 5000.0,
                totalExpense = 1700.0
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun HistoryScreenEmptyPreview() {
    ExpenseTrackerTheme {
        HistoryScreen(
            state = HistoryContract.State(
                transactions = emptyList()
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun HistoryScreenDarkPreview() {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    ExpenseTrackerTheme(darkTheme = true) {
        HistoryScreen(
            state = HistoryContract.State(
                transactions = listOf(
                    Transaction(
                        id = "1",
                        type = TransactionType.EXPENSE,
                        amount = 500.0,
                        category = "Food",
                        paymentType = "UPI",
                        note = "Grocery shopping",
                        date = currentDate,
                        createdAt = Clock.System.now()
                    )
                ),
                totalIncome = 5000.0,
                totalExpense = 500.0
            ),
            onEvent = {}
        )
    }
}
