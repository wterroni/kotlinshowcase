package com.example.kotlinshowcase.feature.password.presentation.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinshowcase.core.theme.Purple40
import com.example.kotlinshowcase.feature.password.domain.model.PasswordOptions

@Composable
fun PasswordOptionsPanel(
    options: PasswordOptions,
    onOptionsChange: (PasswordOptions) -> Unit,
    onGenerateClick: () -> Unit,
    onShowError: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "optionsRotation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = "Password Options",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotation)
            )
        }

        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Length: ${options.length}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${options.length}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Purple40
                    )
                }
                Slider(
                    value = options.length.toFloat(),
                    onValueChange = { newValue ->
                        onOptionsChange(options.copy(length = newValue.toInt()))
                    },
                    valueRange = 4f..32f,
                    steps = 27,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                val totalSelected = listOf(
                    options.includeUppercase,
                    options.includeLowercase,
                    options.includeNumbers,
                    options.includeSymbols
                ).count { it }

                PasswordOptionCheckbox(
                    label = "Uppercase (A-Z)",
                    checked = options.includeUppercase,
                    onCheckedChange = { isChecked ->
                        if (isChecked || totalSelected > 1) {
                            onOptionsChange(options.copy(includeUppercase = isChecked))
                        } else {
                            onShowError("At least one character type must be selected")
                        }
                    }
                )
                PasswordOptionCheckbox(
                    label = "Lowercase (a-z)",
                    checked = options.includeLowercase,
                    onCheckedChange = { isChecked ->
                        if (isChecked || totalSelected > 1) {
                            onOptionsChange(options.copy(includeLowercase = isChecked))
                        } else {
                            onShowError("At least one character type must be selected")
                        }
                    }
                )
                PasswordOptionCheckbox(
                    label = "Numbers (0-9)",
                    checked = options.includeNumbers,
                    onCheckedChange = { isChecked ->
                        if (isChecked || totalSelected > 1) {
                            onOptionsChange(options.copy(includeNumbers = isChecked))
                        } else {
                            onShowError("At least one character type must be selected")
                        }
                    }
                )
                PasswordOptionCheckbox(
                    label = "Symbols (!@#\$...)",
                    checked = options.includeSymbols,
                    onCheckedChange = { isChecked ->
                        if (isChecked || totalSelected > 1) {
                            onOptionsChange(options.copy(includeSymbols = isChecked))
                        } else {
                            onShowError("At least one character type must be selected")
                        }
                    }
                )
            }
        }

        Button(
            onClick = onGenerateClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate Password")
        }
    }
}

@Composable
private fun PasswordOptionCheckbox(
    label: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { if (enabled || checked) onCheckedChange(!checked) }
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordOptionsPanelPreview() {
    PasswordOptionsPanel(
        options = PasswordOptions(),
        onOptionsChange = {},
        onGenerateClick = {},
        onShowError = {}
    )
}
