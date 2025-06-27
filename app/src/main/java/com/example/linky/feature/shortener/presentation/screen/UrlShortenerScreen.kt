package com.example.linky.feature.shortener.presentation.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.presentation.components.UrlListItem
import com.example.linky.feature.shortener.presentation.viewmodel.UrlShortenerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UrlShortenerScreen(
    modifier: Modifier = Modifier,
    viewModel: UrlShortenerViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    
    // Effect to show snackbar when an error occurs
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // In a real scenario, we would show a Snackbar here
            // For now, we just clear the error after a few seconds
            kotlinx.coroutines.delay(3000)
            viewModel.clearError()
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // URL input field
        OutlinedTextField(
            value = uiState.urlText,
            onValueChange = viewModel::onUrlTextChanged,
            label = { Text("URL") },
            placeholder = { Text("https://sou.nu") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.shortenUrl()
                    focusManager.clearFocus()
                }
            ),
            trailingIcon = {
                IconButton(
                    onClick = {
                        viewModel.shortenUrl()
                        focusManager.clearFocus()
                    },
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Shorten URL"
                    )
                }
            },
            enabled = !uiState.isLoading
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Loading indicator
        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Error message
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Recently shortened URLs title
        Text(
            text = "Recently shortened URLs",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // List of recent URLs
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.recentUrls) { shortUrl ->
                UrlListItem(
                    shortUrl = shortUrl,
                    onCopyClick = { url ->
                        copyToClipboard(context, url)
                        Toast.makeText(context, "URL copied to clipboard", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

/**
 * Helper function to copy text to clipboard
 */
private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Shortened URL", text)
    clipboard.setPrimaryClip(clip)
}
