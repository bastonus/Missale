package com.example.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.liturgy.model.LiturgicalDay
import com.example.liturgy.model.LiturgicalItem
import com.example.ui.components.LiturgicalDayBanner
import com.example.ui.components.LiturgicalItemView
import com.example.ui.viewmodel.DisplayLanguage

@Composable
fun ComplineScreen(
    day: LiturgicalDay,
    items: List<LiturgicalItem>,
    showChants: Boolean,
    language: DisplayLanguage,
    fontScale: Float,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onToday: () -> Unit,
    onOpenDatePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .testTag("compline_screen_list"),
        contentPadding = PaddingValues(top = 4.dp, bottom = 80.dp)
    ) {
        // Liturgical Day Banner
        item {
            LiturgicalDayBanner(
                day = day,
                onPreviousDay = onPreviousDay,
                onNextDay = onNextDay,
                onToday = onToday,
                onOpenDatePicker = onOpenDatePicker
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Office Introduction
        item {
            Text(
                text = "Completorium (Rubricae 1960) — Antienne mariale : ${day.marianAntiphon.latinTitle}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp)
            )
        }

        // Office items
        items(items) { liturgicalItem ->
            LiturgicalItemView(
                item = liturgicalItem,
                showChants = showChants,
                language = language,
                fontScale = fontScale
            )
        }
    }
}
