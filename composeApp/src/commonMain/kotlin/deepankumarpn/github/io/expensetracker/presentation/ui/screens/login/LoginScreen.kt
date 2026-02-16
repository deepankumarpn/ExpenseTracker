package deepankumarpn.github.io.expensetracker.presentation.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import deepankumarpn.github.io.expensetracker.presentation.ui.theme.ExpenseTrackerTheme
import androidx.compose.ui.tooling.preview.Preview

/**
 * Login screen composable.
 */
@Composable
fun LoginScreen(
    state: LoginContract.State,
    onEvent: (LoginContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Icon or Logo placeholder
            Surface(
                modifier = Modifier.size(120.dp),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "₹",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Title
            Text(
                text = "ExpenseTracker",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // App Subtitle
            Text(
                text = "Track your expenses and income with ease",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Sign In Button
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Button(
                    onClick = { onEvent(LoginContract.Event.SignInWithGoogleClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !state.isLoading
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Google icon placeholder
                        Text(
                            text = "G",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Sign in with Google",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            // Error Message
            if (state.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { onEvent(LoginContract.Event.RetryClicked) },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Privacy Notice
            Text(
                text = "Your data is stored securely in your Google account",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    ExpenseTrackerTheme {
        LoginScreen(
            state = LoginContract.State(),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun LoginScreenLoadingPreview() {
    ExpenseTrackerTheme {
        LoginScreen(
            state = LoginContract.State(isLoading = true),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun LoginScreenErrorPreview() {
    ExpenseTrackerTheme {
        LoginScreen(
            state = LoginContract.State(
                error = "Failed to sign in. Please check your internet connection and try again."
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun LoginScreenDarkPreview() {
    ExpenseTrackerTheme(darkTheme = true) {
        LoginScreen(
            state = LoginContract.State(),
            onEvent = {}
        )
    }
}
