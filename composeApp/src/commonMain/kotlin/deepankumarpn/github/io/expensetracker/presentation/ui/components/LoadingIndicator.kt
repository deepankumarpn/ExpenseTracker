package deepankumarpn.github.io.expensetracker.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.ExpenseTrackerTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Full screen loading indicator.
 */
@Composable
fun LoadingIndicator(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Compact loading indicator for inline use.
 */
@Composable
fun CompactLoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
fun LoadingIndicatorPreview() {
    ExpenseTrackerTheme {
        LoadingIndicator()
    }
}

@Preview
@Composable
fun LoadingIndicatorCustomMessagePreview() {
    ExpenseTrackerTheme {
        LoadingIndicator(message = "Syncing with Google Sheets...")
    }
}

@Preview
@Composable
fun LoadingIndicatorDarkPreview() {
    ExpenseTrackerTheme(darkTheme = true) {
        LoadingIndicator()
    }
}

@Preview
@Composable
fun CompactLoadingIndicatorPreview() {
    ExpenseTrackerTheme {
        CompactLoadingIndicator()
    }
}
