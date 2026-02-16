package deepankumarpn.github.io.expensetracker.presentation.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import deepankumarpn.github.io.expensetracker.domain.model.*
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.ExpenseTrackerTheme
import deepankumarpn.github.io.expensetracker.utils.getAppInfo
import kotlinx.datetime.Clock
import androidx.compose.ui.tooling.preview.Preview

/**
 * Settings screen composable.
 */
@Composable
fun SettingsScreen(
    state: SettingsContract.State,
    onEvent: (SettingsContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val appInfo = remember { getAppInfo() }
    var showAppInfoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            // Profile Section
            ProfileSection(
                userName = state.userName,
                userEmail = state.userEmail
            )

            Divider()

            // Currency Section
            CurrencySection(
                selectedCurrency = state.selectedCurrency,
                onCurrencyChanged = { onEvent(SettingsContract.Event.CurrencyChanged(it)) }
            )

            Divider()

            // Duration Settings Section
            DurationSettingsSection(
                selectedDuration = state.homeDuration,
                customDays = state.customDays,
                onDurationChanged = { onEvent(SettingsContract.Event.HomeDurationChanged(it)) },
                onCustomDaysChanged = { onEvent(SettingsContract.Event.CustomDaysChanged(it)) }
            )

            Divider()

            // Categories Section
            CategoriesSection(
                categories = state.categories,
                categoriesInUse = state.categoriesInUse,
                onAddCategory = { onEvent(SettingsContract.Event.AddCategory(it)) },
                onDeleteCategory = { onEvent(SettingsContract.Event.DeleteCategory(it)) }
            )

            Divider()

            // Payment Types Section
            PaymentTypesSection(
                paymentTypes = state.paymentTypes,
                paymentTypesInUse = state.paymentTypesInUse,
                onAddPaymentType = { onEvent(SettingsContract.Event.AddPaymentType(it)) },
                onDeletePaymentType = { onEvent(SettingsContract.Event.DeletePaymentType(it)) }
            )

            Divider()

            // App Info Section (Debug only)
            if (appInfo.isDebug) {
                AppInfoSection(
                    onClick = { showAppInfoDialog = true }
                )

                Divider()
            }

            // Sign Out Button
            Button(
                onClick = { onEvent(SettingsContract.Event.SignOut) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out")
            }
        }

    // App Info Dialog
    if (showAppInfoDialog) {
        AppInfoDialog(
            appInfo = appInfo,
            onDismiss = { showAppInfoDialog = false }
        )
    }
}

@Composable
fun ProfileSection(
    userName: String,
    userEmail: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(64.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.firstOrNull()?.toString()?.uppercase() ?: "U",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column {
                Text(
                    text = userName.ifEmpty { "User" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = userEmail.ifEmpty { "user@example.com" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CurrencySection(
    selectedCurrency: CurrencyType,
    onCurrencyChanged: (CurrencyType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Currency",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedCurrency.name} (${selectedCurrency.symbol})",
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
    }
}

@Composable
fun DurationSettingsSection(
    selectedDuration: DurationFilter,
    customDays: Int,
    onDurationChanged: (DurationFilter) -> Unit,
    onCustomDaysChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Home Page Duration",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
            FilterChip(
                selected = selectedDuration == DurationFilter.CUSTOM,
                onClick = { onDurationChanged(DurationFilter.CUSTOM) },
                label = { Text("Custom") }
            )
        }

        if (selectedDuration == DurationFilter.CUSTOM) {
            OutlinedTextField(
                value = customDays.toString(),
                onValueChange = { it.toIntOrNull()?.let(onCustomDaysChanged) },
                label = { Text("Custom Days") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
fun CategoriesSection(
    categories: List<Category>,
    categoriesInUse: Set<String>,
    onAddCategory: (String) -> Unit,
    onDeleteCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Show add dialog */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add category")
            }
        }

        categories.forEach { category ->
            CategoryItem(
                category = category,
                isInUse = categoriesInUse.contains(category.name),
                onDelete = { onDeleteCategory(category.id) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isInUse: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (category.isDefault) {
                    Text(
                        text = "Default category",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (!category.isDefault) {
                IconButton(
                    onClick = onDelete,
                    enabled = !isInUse
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = if (isInUse)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentTypesSection(
    paymentTypes: List<PaymentType>,
    paymentTypesInUse: Set<String>,
    onAddPaymentType: (String) -> Unit,
    onDeletePaymentType: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Payment Types",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* Show add dialog */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add payment type")
            }
        }

        paymentTypes.forEach { paymentType ->
            PaymentTypeItem(
                paymentType = paymentType,
                isInUse = paymentTypesInUse.contains(paymentType.name),
                onDelete = { onDeletePaymentType(paymentType.id) }
            )
        }
    }
}

@Composable
fun PaymentTypeItem(
    paymentType: PaymentType,
    isInUse: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = paymentType.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (paymentType.isDefault) {
                    Text(
                        text = "Default payment type",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (!paymentType.isDefault) {
                IconButton(
                    onClick = onDelete,
                    enabled = !isInUse
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = if (isInUse)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AppInfoSection(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "App Information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "App Info",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "App Details",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "View app information (Debug)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AppInfoDialog(
    appInfo: deepankumarpn.github.io.expensetracker.utils.AppInfo,
    onDismiss: () -> Unit
) {
    var advertisingId by remember { mutableStateOf<String?>(null) }
    var installationId by remember { mutableStateOf<String?>(null) }
    var androidId by remember { mutableStateOf<String?>(null) }
    var launchSessionId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        advertisingId = appInfo.getAdvertisingId()
        installationId = appInfo.getInstallationId()
        androidId = appInfo.getAndroidId()
        launchSessionId = appInfo.getLaunchSessionId()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("App Information")
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "App Details",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                InfoRow(label = "Version", value = "${appInfo.versionName} (${appInfo.versionCode})")
                InfoRow(label = "Package", value = appInfo.packageName)
                InfoRow(label = "Platform", value = appInfo.platform)
                InfoRow(label = "OS Version", value = appInfo.osVersion)
                InfoRow(label = "Device", value = appInfo.deviceModel)
                InfoRow(label = "Country", value = appInfo.getCountryCode())
                InfoRow(label = "Currency", value = "${appInfo.getCurrencyCode()} (${appInfo.getDefaultCurrency().symbol})")
                InfoRow(label = "Build Type", value = if (appInfo.isDebug) "Debug" else "Release")

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Identity IDs",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                InfoRow(
                    label = "Advertising ID",
                    value = advertisingId ?: "Loading..."
                )
                InfoRow(
                    label = "Installation ID",
                    value = installationId?.take(20)?.plus("...") ?: "Loading..."
                )
                InfoRow(
                    label = "Android ID",
                    value = androidId?.take(16) ?: "Loading..."
                )
                InfoRow(
                    label = "Session ID",
                    value = launchSessionId?.take(20)?.plus("...") ?: "Loading..."
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    ExpenseTrackerTheme {
        SettingsScreen(
            state = SettingsContract.State(
                userName = "John Doe",
                userEmail = "john.doe@example.com",
                selectedCurrency = CurrencyType.getDefaultCurrency(),
                categories = listOf(
                    Category("1", "Food", true, Clock.System.now()),
                    Category("2", "Travel", true, Clock.System.now()),
                    Category("3", "Entertainment", false, Clock.System.now())
                ),
                paymentTypes = listOf(
                    PaymentType("1", "Cash", true, Clock.System.now()),
                    PaymentType("2", "UPI", true, Clock.System.now()),
                    PaymentType("3", "HDFC Card", false, Clock.System.now())
                ),
                homeDuration = DurationFilter.MONTHLY
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun SettingsScreenDarkPreview() {
    ExpenseTrackerTheme(darkTheme = true) {
        SettingsScreen(
            state = SettingsContract.State(
                userName = "Jane Smith",
                userEmail = "jane.smith@example.com",
                selectedCurrency = CurrencyType.USD,
                homeDuration = DurationFilter.CUSTOM,
                customDays = 45
            ),
            onEvent = {}
        )
    }
}
