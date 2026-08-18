package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.liturgy.model.LiturgicalDay
import com.example.liturgy.model.LiturgicalItem
import com.example.ui.components.LiturgicalDayBanner
import com.example.ui.components.LiturgicalItemView
import com.example.ui.viewmodel.DisplayLanguage

@Composable
fun MissalScreen(
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
            .testTag("missal_screen_list"),
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

        // Commemorations if any
        if (day.commemorations.isNotEmpty()) {
            item {
                day.commemorations.forEach { commemoration ->
                    LiturgicalItemView(
                        item = LiturgicalItem.Rubric(commemoration),
                        showChants = showChants,
                        language = language,
                        fontScale = fontScale
                    )
                }
            }
        }

        // List of Ordo Missae items
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
