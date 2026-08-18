package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.liturgy.data.KyrialeLibrary
import com.example.liturgy.gabc.GregorianScoreView

@Composable
fun ChantsScreen(
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Tous") }
    val categories = listOf("Tous", "Asperges", "Kyrie", "Sanctus", "Agnus Dei", "Credo")

    val filteredChants = remember(selectedCategory) {
        if (selectedCategory == "Tous") {
            KyrialeLibrary.allChants
        } else {
            KyrialeLibrary.allChants.filter { it.part.equals(selectedCategory, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .testTag("chants_screen_list"),
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Anthologie Grégorienne (Kyriale Romanum)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Rendu natif vectoriel sur portée à 4 lignes selon la notation carrée de Solesmes (GABC).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Category Filter Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.testTag("filter_chip_$category")
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Chants List with GABC Canvas Renderer
        items(filteredChants) { chant ->
            GregorianScoreView(
                rawGabc = chant.gabc,
                title = chant.title,
                mode = chant.mode,
                translation = chant.translation,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
