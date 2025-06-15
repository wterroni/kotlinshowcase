package com.example.kotlinshowcase.feature.amiibo.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListState
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel

@Composable
fun AmiiboListScreen(
    viewModel: AmiiboListViewModel,
    onAmiiboClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        viewModel.searchAmiibos(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Amiibos") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (val currentState = state) {
            is AmiiboListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AmiiboListState.Success -> {
                if (currentState.amiibos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Amiibos found")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(currentState.amiibos) { amiibo ->
                            AmiiboItem(
                                amiibo = amiibo,
                                onClick = { onAmiiboClick(amiibo.name) }
                            )
                        }
                    }
                }
            }
            is AmiiboListState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Error: ${currentState.message}")
                        Button(onClick = { viewModel.loadAmiibos() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AmiiboItem(
    amiibo: com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = amiibo.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Character: ${amiibo.character}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Game Series: ${amiibo.gameSeries}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
