package deepankumarpn.github.io.expensetracker.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import deepankumarpn.github.io.expensetracker.domain.model.Category
import deepankumarpn.github.io.expensetracker.domain.model.CurrencyType
import deepankumarpn.github.io.expensetracker.domain.model.PaymentType
import deepankumarpn.github.io.expensetracker.domain.model.TransactionType
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.*
import deepankumarpn.github.io.expensetracker.utils.formatAmountWithCommas
import deepankumarpn.github.io.expensetracker.utils.toDisplayString
import kotlinx.datetime.*
import androidx.compose.ui.tooling.preview.Preview

/**
 * Home screen composable.
 */
@Composable
fun HomeScreen(
    state: HomeContract.State,
    onEvent: (HomeContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentDate = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    val displayDate = state.selectedDate ?: currentDate

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            // Date Header (Top Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clickable { onEvent(HomeContract.Event.ShowDatePicker) }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Date: ${displayDate.toDisplayString()}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Select date",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Transaction Type Selector
            TransactionTypeSelector(
                selectedType = state.transactionType,
                onTypeSelected = { onEvent(HomeContract.Event.TypeSelected(it)) }
            )

            // Transaction Form
            TransactionForm(
                state = state,
                onEvent = onEvent,
                currency = state.currency
            )

            // Submit Button
            Button(
                onClick = { onEvent(HomeContract.Event.SubmitTransaction) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isSubmitting && state.amount.isNotBlank(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        "Add Transaction",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Summary Section (Moved to Bottom)
            SummarySection(
                totalIncome = state.totalIncome,
                totalExpense = state.totalExpense,
                durationLabel = state.durationLabel,
                currency = state.currency
            )
        }

    // Date Picker Dialog
    if (state.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = (state.selectedDate ?: currentDate).toEpochDays().toLong() * 24 * 60 * 60 * 1000
        )

        DatePickerDialog(
            onDismissRequest = { onEvent(HomeContract.Event.DismissDatePicker) },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val epochDays = (millis / (24 * 60 * 60 * 1000)).toInt()
                            val selectedDate = LocalDate.fromEpochDays(epochDays)
                            onEvent(HomeContract.Event.DateSelected(selectedDate))
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onEvent(HomeContract.Event.DismissDatePicker) }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun SummarySection(
    totalIncome: Double,
    totalExpense: Double,
    durationLabel: String,
    currency: CurrencyType,
    modifier: Modifier = Modifier
) {
    val balance = totalIncome - totalExpense
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(GradientStart, GradientMiddle, GradientEnd)
    )

    // Main Balance Card with Gradient
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gradientBrush)
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Duration Label
            Text(
                text = durationLabel,
                style = MaterialTheme.typography.labelLarge,
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f)
            )

            // Total Balance
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Total Balance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = balance.formatAmountWithCommas(currency),
                    style = MaterialTheme.typography.displaySmall,
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Income & Expense Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Income Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.2f))
                        .padding(16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Income",
                                tint = androidx.compose.ui.graphics.Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Income",
                                style = MaterialTheme.typography.bodySmall,
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Text(
                            text = totalIncome.formatAmountWithCommas(currency),
                            style = MaterialTheme.typography.titleLarge,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Expense Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.2f))
                        .padding(16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Expense",
                                tint = androidx.compose.ui.graphics.Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Expense",
                                style = MaterialTheme.typography.bodySmall,
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Text(
                            text = totalExpense.formatAmountWithCommas(currency),
                            style = MaterialTheme.typography.titleLarge,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionTypeSelector(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedType == TransactionType.EXPENSE,
            onClick = { onTypeSelected(TransactionType.EXPENSE) },
            label = { Text("Expense") },
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Expense"
                )
            }
        )

        FilterChip(
            selected = selectedType == TransactionType.INCOME,
            onClick = { onTypeSelected(TransactionType.INCOME) },
            label = { Text("Income") },
            modifier = Modifier.weight(1f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Income"
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionForm(
    state: HomeContract.State,
    onEvent: (HomeContract.Event) -> Unit,
    currency: CurrencyType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Amount Field
        OutlinedTextField(
            value = state.amount,
            onValueChange = { onEvent(HomeContract.Event.AmountChanged(it)) },
            label = { Text("Amount") },
            placeholder = { Text("0.00") },
            leadingIcon = {
                Text(
                    text = currency.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        // Note Field
        OutlinedTextField(
            value = state.note,
            onValueChange = { onEvent(HomeContract.Event.NoteChanged(it)) },
            label = { Text("Note (optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        // Payment Type Selector (Chips)
        if (state.paymentTypes.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Payment Type",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.paymentTypes.forEach { paymentType ->
                        FilterChip(
                            selected = state.selectedPaymentType?.id == paymentType.id,
                            onClick = { onEvent(HomeContract.Event.PaymentTypeSelected(paymentType)) },
                            label = { Text(paymentType.name) }
                        )
                    }
                }
            }
        }

        // Category Selector (Chips)
        if (state.categories.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.categories.forEach { category ->
                        FilterChip(
                            selected = state.selectedCategory?.id == category.id,
                            onClick = { onEvent(HomeContract.Event.CategorySelected(category)) },
                            label = { Text(category.name) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    ExpenseTrackerTheme {
        HomeScreen(
            state = HomeContract.State(
                totalIncome = 45000.0,
                totalExpense = 32500.0,
                durationLabel = "This Month",
                selectedDate = currentDate,
                amount = "500",
                note = "Grocery shopping",
                categories = listOf(
                    Category("1", "Food", true, Clock.System.now()),
                    Category("2", "Travel", true, Clock.System.now())
                ),
                paymentTypes = listOf(
                    PaymentType("1", "Cash", true, Clock.System.now()),
                    PaymentType("2", "UPI", true, Clock.System.now())
                )
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenDarkPreview() {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    ExpenseTrackerTheme(darkTheme = true) {
        HomeScreen(
            state = HomeContract.State(
                totalIncome = 45000.0,
                totalExpense = 32500.0,
                durationLabel = "This Month",
                selectedDate = currentDate,
                transactionType = TransactionType.INCOME
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenLoadingPreview() {
    ExpenseTrackerTheme {
        HomeScreen(
            state = HomeContract.State(
                isLoading = true,
                isSubmitting = true
            ),
            onEvent = {}
        )
    }
}
