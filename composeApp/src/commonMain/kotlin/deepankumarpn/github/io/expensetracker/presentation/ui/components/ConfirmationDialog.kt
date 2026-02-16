package deepankumarpn.github.io.expensetracker.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.ExpenseTrackerTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Confirmation dialog component.
 */
@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    icon: ImageVector = Icons.Default.Warning,
    isDestructive: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = if (isDestructive) {
                    ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                } else {
                    ButtonDefaults.textButtonColors()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    var showDialog by remember { mutableStateOf(true) }

    ExpenseTrackerTheme {
        if (showDialog) {
            ConfirmationDialog(
                title = "Delete Category",
                message = "Are you sure you want to delete this category? This action cannot be undone.",
                confirmText = "Delete",
                onConfirm = { showDialog = false },
                onDismiss = { showDialog = false },
                isDestructive = true
            )
        }
    }
}

@Preview
@Composable
fun ConfirmationDialogNonDestructivePreview() {
    var showDialog by remember { mutableStateOf(true) }

    ExpenseTrackerTheme {
        if (showDialog) {
            ConfirmationDialog(
                title = "Save Changes",
                message = "Do you want to save your changes before leaving?",
                confirmText = "Save",
                dismissText = "Discard",
                onConfirm = { showDialog = false },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Preview
@Composable
fun ConfirmationDialogDarkPreview() {
    var showDialog by remember { mutableStateOf(true) }

    ExpenseTrackerTheme(darkTheme = true) {
        if (showDialog) {
            ConfirmationDialog(
                title = "Sign Out",
                message = "Are you sure you want to sign out?",
                confirmText = "Sign Out",
                onConfirm = { showDialog = false },
                onDismiss = { showDialog = false },
                isDestructive = true
            )
        }
    }
}
