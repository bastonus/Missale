package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liturgy.gabc.GregorianScoreView
import com.example.liturgy.model.LiturgicalDay
import com.example.liturgy.model.LiturgicalItem
import com.example.liturgy.model.Speaker
import com.example.ui.theme.RubricRed
import com.example.ui.viewmodel.DisplayLanguage
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Top bar liturgical day header banner showing date, season, rank, color, and date navigator.
 */
@Composable
fun LiturgicalDayBanner(
    day: LiturgicalDay,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onToday: () -> Unit,
    onOpenDatePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH)
    val formattedDate = day.date.format(formatter).replaceFirstChar { it.uppercase() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("liturgical_day_banner"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, RubricRed.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Illuminated Liturgical Color Ribbon
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(135.dp)
                    .background(day.color.composeColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Date Navigation Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onPreviousDay,
                        modifier = Modifier.size(36.dp).testTag("prev_day_button")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Jour précédent",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onNextDay,
                        modifier = Modifier.size(36.dp).testTag("next_day_button")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Jour suivant",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Feast Title & Latin Subtitle
                Text(
                    text = day.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (day.latinTitle.isNotEmpty()) {
                    Text(
                        text = day.latinTitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Liturgical Badges: Class, Color, Season, Kyriale
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Color Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = day.color.composeColor,
                        shadowElevation = 1.dp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(day.color.composeTextColor, CircleShape)
                            )
                            Text(
                                text = day.color.frenchName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = day.color.composeTextColor
                            )
                        }
                    }

                    // Liturgical Class Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "Classe ${day.liturgicalClass.romanNumeral}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // Kyriale Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "Kyriale ${day.suggestedKyriale.romanNumber}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Date Picker Quick Button
                    IconButton(
                        onClick = onOpenDatePicker,
                        modifier = Modifier.size(32.dp).testTag("date_picker_button")
                    ) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Choisir une date",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Liturgical item renderer handles Rubrics (in red), Bilingual texts, Section headers, and GABC chants.
 */
@Composable
fun LiturgicalItemView(
    item: LiturgicalItem,
    showChants: Boolean,
    language: DisplayLanguage,
    fontScale: Float,
    modifier: Modifier = Modifier
) {
    when (item) {
        is LiturgicalItem.Rubric -> {
            Text(
                text = item.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (13.5 * fontScale).sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = (18 * fontScale).sp
                ),
                color = RubricRed,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp)
            )
        }

        is LiturgicalItem.HeaderSection -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.latin.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = (16 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                if (!item.vernacular.isNullOrBlank() && language != DisplayLanguage.LATIN_ONLY) {
                    Text(
                        text = item.vernacular,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = (12 * fontScale).sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    modifier = Modifier.width(60.dp),
                    thickness = 1.5.dp,
                    color = RubricRed
                )
            }
        }

        is LiturgicalItem.Title -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    text = item.latin,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = (15 * fontScale).sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                if (!item.vernacular.isNullOrBlank() && language != DisplayLanguage.LATIN_ONLY) {
                    Text(
                        text = item.vernacular,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (13 * fontScale).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (!item.subtitle.isNullOrBlank()) {
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = (11 * fontScale).sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = RubricRed
                    )
                }
            }
        }

        is LiturgicalItem.BilingualText -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 2.dp)
            ) {
                if (item.speaker != null) {
                    Text(
                        text = "${item.speaker.symbol} ${item.speaker.label}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = (12 * fontScale).sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = RubricRed,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                // Latin text
                Text(
                    text = item.latin,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (14.5 * fontScale).sp,
                        lineHeight = (21 * fontScale).sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Vernacular translation (if not in Latin-only mode)
                if (language != DisplayLanguage.LATIN_ONLY && item.vernacular.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.vernacular,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = (13 * fontScale).sp,
                            lineHeight = (18 * fontScale).sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        is LiturgicalItem.VersicleResponse -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp, horizontal = 2.dp)
            ) {
                // Versicle
                Row {
                    Text(
                        text = "℣. ",
                        fontWeight = FontWeight.Bold,
                        color = RubricRed,
                        fontSize = (14 * fontScale).sp
                    )
                    Column {
                        Text(
                            text = item.versicleLatin,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = (14 * fontScale).sp,
                                fontFamily = FontFamily.Serif
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (language != DisplayLanguage.LATIN_ONLY) {
                            Text(
                                text = item.versicleVernacular,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = (12.5 * fontScale).sp,
                                    fontStyle = FontStyle.Italic
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Response
                Row {
                    Text(
                        text = "℟. ",
                        fontWeight = FontWeight.Bold,
                        color = RubricRed,
                        fontSize = (14 * fontScale).sp
                    )
                    Column {
                        Text(
                            text = item.responseLatin,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = (14 * fontScale).sp,
                                fontFamily = FontFamily.Serif
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (language != DisplayLanguage.LATIN_ONLY) {
                            Text(
                                text = item.responseVernacular,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = (12.5 * fontScale).sp,
                                    fontStyle = FontStyle.Italic
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        is LiturgicalItem.ChantScore -> {
            if (showChants) {
                GregorianScoreView(
                    rawGabc = item.gabc,
                    title = item.title,
                    mode = item.mode,
                    translation = if (language != DisplayLanguage.LATIN_ONLY) item.translation else null,
                    modifier = modifier
                )
            } else {
                // Text-only fallback for chant score
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "♪ " + item.title + if (item.mode != null) " (Ton ${item.mode})" else "",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = (14 * fontScale).sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (!item.translation.isNullOrBlank() && language != DisplayLanguage.LATIN_ONLY) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = item.translation,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = (12.5 * fontScale).sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        is LiturgicalItem.InstructionNote -> {
            Text(
                text = item.note,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = (12 * fontScale).sp,
                    fontStyle = FontStyle.Italic
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier.padding(vertical = 2.dp)
            )
        }
    }
}
