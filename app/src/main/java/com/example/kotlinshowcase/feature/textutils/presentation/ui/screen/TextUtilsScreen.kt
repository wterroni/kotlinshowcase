package com.example.kotlinshowcase.feature.textutils.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.kotlinshowcase.feature.textutils.domain.usecase.CapitalizeWordsUseCase
import org.koin.compose.koinInject

@Composable
private fun AnimatedClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "clearButtonScale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        label = "clearButtonAlpha"
    )
    
    IconButton(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Clear",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = alpha)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextUtilsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val capitalizeWordsUseCase: CapitalizeWordsUseCase = koinInject()

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                TopAppBar(
                    title = { 
                        Text(
                            "Text Utils",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        ) 
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.animateContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .animateContentSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            var isTextFieldFocused by remember { mutableStateOf(false) }
            
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { 
                    Text(
                        "Enter text",
                        style = MaterialTheme.typography.bodyMedium
                    ) 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isTextFieldFocused = it.isFocused },
                singleLine = false,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary
                ),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = inputText.isNotBlank(),
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        AnimatedClearButton(
                            onClick = { 
                                inputText = "" 
                                keyboardController?.hide()
                            },
                            visible = inputText.isNotBlank()
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = inputText.isNotBlank(),
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = capitalizeWordsUseCase(inputText),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

