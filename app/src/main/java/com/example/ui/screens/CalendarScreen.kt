package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liturgy.calendar.LiturgicalCalendar
import com.example.liturgy.model.LiturgicalDay
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var viewedMonth by remember(selectedDate) { mutableStateOf(selectedDate.withDayOfMonth(1)) }

    val daysInMonth = remember(viewedMonth) {
        val count = viewedMonth.lengthOfMonth()
        (1..count).map { day ->
            val date = viewedMonth.withDayOfMonth(day)
            LiturgicalCalendar.computeDay(date)
        }
    }

    // Computus data for the current year
    val year = viewedMonth.year
    val easter = remember(year) { LiturgicalCalendar.getEasterSunday(year) }
    val advent1 = remember(year) { LiturgicalCalendar.getFirstSundayOfAdvent(year) }
    val christTheKing = remember(year) { LiturgicalCalendar.getChristTheKingSunday(year) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .testTag("calendar_screen_list"),
        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
    ) {
        // Month / Year Navigation Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewedMonth = viewedMonth.minusMonths(1) },
                        modifier = Modifier.testTag("prev_month_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Mois précédent")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${viewedMonth.month.getDisplayName(TextStyle.FULL, Locale.FRENCH).replaceFirstChar { it.uppercase() }} ${viewedMonth.year}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Calendrier Romain perpétuel (1960)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { viewedMonth = viewedMonth.plusMonths(1) },
                        modifier = Modifier.testTag("next_month_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Mois suivant")
                    }
                }
            }
        }

        // Computus Summary Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Comput Ecclésiastique (Année $year)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Mercredi des Cendres : ${easter.minusDays(46).format(DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH))}\n" +
                                "• Dimanche de Pâques : ${easter.format(DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH))}\n" +
                                "• Ascension : ${easter.plusDays(39).format(DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH))}\n" +
                                "• Pentecôte : ${easter.plusDays(49).format(DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH))}\n" +
                                "• Fête-Dieu : ${easter.plusDays(60).format(DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH))}\n" +
                                "• Christ-Roi : ${christTheKing.format(DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH))}\n" +
                                "• Ier Dimanche de l'Avent : ${advent1.format(DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH))}",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Month Days List
        items(daysInMonth) { day ->
            val isSelected = day.date == selectedDate
            CalendarDayRow(
                day = day,
                isSelected = isSelected,
                onClick = { onSelectDate(day.date) }
            )
        }
    }
}

@Composable
private fun CalendarDayRow(
    day: LiturgicalDay,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("d", Locale.FRENCH)
    val dayNameFormatter = DateTimeFormatter.ofPattern("EEE", Locale.FRENCH)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable(onClick = onClick)
            .testTag("calendar_day_${day.date}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day Number & Day Name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(38.dp)
            ) {
                Text(
                    text = day.date.format(dayFormatter),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = day.date.format(dayNameFormatter).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Liturgical Color indicator
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(day.color.composeColor, CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Feast Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = day.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (day.liturgicalClass.precedence <= 2 || day.isSunday) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Classe ${day.liturgicalClass.romanNumeral} • ${day.color.frenchName} • Kyriale ${day.suggestedKyriale.romanNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Sélectionné",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
