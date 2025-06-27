package com.example.linky.feature.shortener.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linky.core.theme.LinkyTheme
import com.example.linky.feature.shortener.domain.model.ShortUrl

/**
 * Component that displays a shortened URL item in the list
 */
@Composable
fun UrlListItem(
    shortUrl: ShortUrl,
    modifier: Modifier = Modifier,
    onCopyClick: (String) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Original URL
            Text(
                text = shortUrl.originalUrl,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Shortened URL with copy button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = shortUrl.shortUrl,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                TextButton(
                    onClick = { onCopyClick(shortUrl.shortUrl) },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("Copy")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UrlListItemPreview() {
    LinkyTheme {
        UrlListItem(
            shortUrl = ShortUrl(
                originalUrl = "https://www.example.com/very/long/url/that/should/be/truncated",
                alias = "abc123",
                shortUrl = "https://sou.nu/abc123"
            )
        )
    }
}
