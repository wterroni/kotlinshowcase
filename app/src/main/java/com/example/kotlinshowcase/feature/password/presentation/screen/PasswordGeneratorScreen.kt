package com.example.kotlinshowcase.feature.password.presentation.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.kotlinshowcase.feature.password.domain.model.PasswordStrength
import org.koin.androidx.compose.koinViewModel
import com.example.kotlinshowcase.feature.password.presentation.viewmodel.PasswordGeneratorViewModel
import com.example.kotlinshowcase.feature.password.presentation.viewmodel.PasswordGeneratorUiState
import com.example.kotlinshowcase.feature.password.presentation.ui.components.PasswordDisplay
import com.example.kotlinshowcase.feature.password.presentation.ui.components.PasswordOptionsPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(
    onBackClick: () -> Unit,
    viewModel: PasswordGeneratorViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Password Generator") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Password Display
            when (val state = uiState) {
                is PasswordGeneratorUiState.Success -> {
                    val password = state.password
                    PasswordDisplay(
                        password = password.password,
                        strength = password.strength,
                        strengthColor = when (password.strength) {
                            PasswordStrength.WEAK -> Color.Red
                            PasswordStrength.FAIR -> Color(0xFFFFA500) // Orange
                            PasswordStrength.GOOD -> Color(0xFF90EE90) // Light Green
                            PasswordStrength.STRONG -> Color.Green
                        },
                        onCopyClick = {
                            clipboardManager.setText(AnnotatedString(password.password))
                        }
                    )
                }
                is PasswordGeneratorUiState.Error -> {
                    // Show error state
                }
                PasswordGeneratorUiState.Loading -> {
                    // Show loading state
                }
            }

            // Options Panel
            PasswordOptionsPanel(
                options = viewModel.passwordOptions,
                onOptionsChange = { newOptions ->
                    viewModel.updateOptions(newOptions)
                },
                onGenerateClick = {
                    viewModel.generateNewPassword()
                }
            )
            
            // Tips
            Text(
                text = "• Use at least 12 characters\n• Include numbers and symbols\n• Avoid common words or patterns",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}
