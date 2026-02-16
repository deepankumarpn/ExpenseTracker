package deepankumarpn.github.io.expensetracker.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.ExpenseTrackerTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Error dialog component.
 */
@Composable
fun ErrorDialog(
    title: String = "Error",
    message: String,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null,
    icon: ImageVector = Icons.Default.Error
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            if (onRetry != null) {
                TextButton(onClick = onRetry) {
                    Text("Retry")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}

@Preview
@Composable
fun ErrorDialogPreview() {
    var showDialog by remember { mutableStateOf(true) }

    ExpenseTrackerTheme {
        if (showDialog) {
            ErrorDialog(
                message = "Failed to load data. Please check your internet connection and try again.",
                onDismiss = { showDialog = false },
                onRetry = { showDialog = false }
            )
        }
    }
}

@Preview
@Composable
fun ErrorDialogNoRetryPreview() {
    var showDialog by remember { mutableStateOf(true) }

    ExpenseTrackerTheme {
        if (showDialog) {
            ErrorDialog(
                message = "An unexpected error occurred.",
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Preview
@Composable
fun ErrorDialogDarkPreview() {
    var showDialog by remember { mutableStateOf(true) }

    ExpenseTrackerTheme(darkTheme = true) {
        if (showDialog) {
            ErrorDialog(
                title = "Network Error",
                message = "Unable to connect to Google Sheets. Please check your internet connection.",
                onDismiss = { showDialog = false },
                onRetry = { showDialog = false }
            )
        }
    }
}
