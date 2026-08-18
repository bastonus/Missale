package com.example.liturgy.gabc

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * Reusable cached paint objects to avoid heap/ashmem allocation overhead during Canvas draw passes.
 */
private val cachedTextPaint = Paint().apply {
    isAntiAlias = true
    typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
}

private val cachedAccidentalPaint = Paint().apply {
    isAntiAlias = true
    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
}

/**
 * Represents a single wrapped line of a Gregorian score with its own 4-line staff,
 * clef, subset of words, and optional custos at the end.
 */
data class ScoreStaffLine(
    val words: List<GabcWord>,
    val clef: GabcClef,
    val nextLineFirstPitch: Int? = null
)

/**
 * High-quality Jetpack Compose Composable for Gregorian Chant (GABC) rendering.
 * Supports:
 * 1. Multi-line wrapped systems (Mode Page / Livre de chant / Solesmes) fitting screen width with Custos (guidon)
 * 2. Horizontal scroll mode
 * 3. Interactive zoom controls
 */
@Composable
fun GregorianScoreView(
    rawGabc: String,
    title: String? = null,
    mode: String? = null,
    translation: String? = null,
    modifier: Modifier = Modifier,
    staffColor: Color = Color(0xFFB71C1C), // Solesmes traditional crimson red staff
    noteColor: Color = MaterialTheme.colorScheme.onSurface,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val score = remember(rawGabc) { GabcParser.parse(rawGabc) }
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var wrapLines by remember { mutableStateOf(true) } // Default: True (Multi-line wrapped page view)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .testTag("gregorian_score_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header: Title, Mode Badge, Wrap toggle & Zoom controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = staffColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = title ?: score.title.ifEmpty { "Chant Grégorien" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val displayMode = mode ?: score.mode
                    if (displayMode.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text(
                                text = "Ton $displayMode",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Wrap / Scroll toggle
                    IconButton(
                        onClick = { wrapLines = !wrapLines },
                        modifier = Modifier.size(32.dp).testTag("toggle_wrap_button")
                    ) {
                        Icon(
                            imageVector = if (wrapLines) Icons.Default.FormatAlignLeft else Icons.Default.ViewStream,
                            contentDescription = if (wrapLines) "Mode page (portées enveloppées)" else "Mode défilement horizontal",
                            modifier = Modifier.size(18.dp),
                            tint = if (wrapLines) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { zoomScale = (zoomScale - 0.15f).coerceAtLeast(0.75f) },
                        modifier = Modifier.size(32.dp).testTag("zoom_out_button")
                    ) {
                        Icon(Icons.Default.ZoomOut, contentDescription = "Zoom out", modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = { zoomScale = (zoomScale + 0.15f).coerceAtMost(1.6f) },
                        modifier = Modifier.size(32.dp).testTag("zoom_in_button")
                    ) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Zoom in", modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val density = LocalDensity.current
            val baseUnit = 11.5f * zoomScale
            val staffLineSpacing = baseUnit * 1.05f
            val staffTopMargin = 32f * zoomScale
            val staffHeight = staffLineSpacing * 3
            val systemHeight = staffTopMargin + staffHeight + (32f * zoomScale)

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                val availableWidthPx = with(density) { maxWidth.toPx() }

                if (wrapLines) {
                    // Multi-line wrapped systems layout
                    val lines = remember(score, availableWidthPx, baseUnit) {
                        layoutScoreIntoLines(score, availableWidthPx, baseUnit)
                    }

                    val totalCanvasHeightPx = lines.size * systemHeight
                    val totalCanvasHeightDp = with(density) { totalCanvasHeightPx.toDp() }

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(totalCanvasHeightDp)
                            .testTag("gregorian_score_canvas_multiline")
                    ) {
                        drawMultiLineGregorianScore(
                            lines = lines,
                            baseUnit = baseUnit,
                            staffLineSpacing = staffLineSpacing,
                            staffTopMargin = staffTopMargin,
                            systemHeight = systemHeight,
                            staffColor = staffColor,
                            noteColor = noteColor,
                            textColor = textColor,
                            zoomScale = zoomScale
                        )
                    }
                } else {
                    // Single horizontal scrolling strip layout
                    val scrollState = rememberScrollState()
                    val estimatedWidth = calculateScoreWidth(score, baseUnit)
                    val canvasWidthDp = with(density) { estimatedWidth.toDp() }
                    val canvasHeightDp = with(density) { systemHeight.toDp() }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                    ) {
                        Canvas(
                            modifier = Modifier
                                .height(canvasHeightDp)
                                .width(canvasWidthDp.coerceAtLeast(340.dp))
                                .testTag("gregorian_score_canvas_scroll")
                        ) {
                            val singleLine = ScoreStaffLine(words = score.words, clef = score.clef, nextLineFirstPitch = null)
                            drawStaffLine(
                                line = singleLine,
                                lineIndex = 0,
                                startY = 0f,
                                width = size.width,
                                baseUnit = baseUnit,
                                staffLineSpacing = staffLineSpacing,
                                staffTopMargin = staffTopMargin,
                                staffColor = staffColor,
                                noteColor = noteColor,
                                textColor = textColor,
                                zoomScale = zoomScale
                            )
                        }
                    }
                }
            }

            // Translation / Vernacular commentary
            if (!translation.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Breaks the GABC score into wrapped lines of systems based on available width.
 */
private fun layoutScoreIntoLines(
    score: GabcScore,
    availableWidth: Float,
    baseUnit: Float
): List<ScoreStaffLine> {
    if (score.words.isEmpty()) {
        return listOf(ScoreStaffLine(emptyList(), score.clef, null))
    }

    val lines = mutableListOf<ScoreStaffLine>()
    val clefWidth = baseUnit * 4.5f
    val custosReserveWidth = baseUnit * 3.5f
    val maxLineWidth = max(availableWidth - 10f, baseUnit * 25f)

    var currentWords = mutableListOf<GabcWord>()
    var currentLineWidth = clefWidth

    for (word in score.words) {
        val wordWidth = calculateWordWidth(word, baseUnit)

        if (currentWords.isNotEmpty() && (currentLineWidth + wordWidth + custosReserveWidth > maxLineWidth)) {
            // Line break before this word
            lines.add(ScoreStaffLine(words = currentWords.toList(), clef = score.clef))
            currentWords = mutableListOf(word)
            currentLineWidth = clefWidth + wordWidth
        } else {
            currentWords.add(word)
            currentLineWidth += wordWidth + (baseUnit * 1.2f)
        }
    }

    if (currentWords.isNotEmpty()) {
        lines.add(ScoreStaffLine(words = currentWords.toList(), clef = score.clef))
    }

    // Compute Custos (guidon) pitch for each line (the pitch of the 1st note of next line)
    return lines.mapIndexed { index, line ->
        val nextFirstPitch = if (index < lines.size - 1) {
            findFirstPitchOfLine(lines[index + 1])
        } else null
        line.copy(nextLineFirstPitch = nextFirstPitch)
    }
}

private fun findFirstPitchOfLine(line: ScoreStaffLine): Int? {
    for (word in line.words) {
        for (syllable in word.syllables) {
            for (cluster in syllable.notes) {
                if (cluster.notes.isNotEmpty()) {
                    return cluster.notes.first().pitchIndex
                }
            }
        }
    }
    return null
}

private fun calculateWordWidth(word: GabcWord, baseUnit: Float): Float {
    var width = 0f
    for (syllable in word.syllables) {
        val textWidth = max(syllable.text.length * baseUnit * 0.9f, 20f)
        val notesWidth = syllable.notes.sumOf { cluster ->
            max(cluster.notes.size * baseUnit * 1.15, baseUnit * 1.4)
        }.toFloat()
        val syllableWidth = max(textWidth, notesWidth) + (if (syllable.barLine != null) baseUnit * 2.2f else baseUnit * 1.0f)
        width += syllableWidth
    }
    return width
}

private fun calculateScoreWidth(score: GabcScore, baseUnit: Float): Float {
    var width = 80f * (baseUnit / 11.5f) // Initial clef space
    for (word in score.words) {
        width += calculateWordWidth(word, baseUnit) + (baseUnit * 1.4f)
    }
    return max(width + 80f, 600f)
}

private fun DrawScope.drawMultiLineGregorianScore(
    lines: List<ScoreStaffLine>,
    baseUnit: Float,
    staffLineSpacing: Float,
    staffTopMargin: Float,
    systemHeight: Float,
    staffColor: Color,
    noteColor: Color,
    textColor: Color,
    zoomScale: Float
) {
    lines.forEachIndexed { index, staffLine ->
        val lineStartY = index * systemHeight
        drawStaffLine(
            line = staffLine,
            lineIndex = index,
            startY = lineStartY,
            width = size.width,
            baseUnit = baseUnit,
            staffLineSpacing = staffLineSpacing,
            staffTopMargin = staffTopMargin,
            staffColor = staffColor,
            noteColor = noteColor,
            textColor = textColor,
            zoomScale = zoomScale
        )
    }
}

private fun DrawScope.drawStaffLine(
    line: ScoreStaffLine,
    lineIndex: Int,
    startY: Float,
    width: Float,
    baseUnit: Float,
    staffLineSpacing: Float,
    staffTopMargin: Float,
    staffColor: Color,
    noteColor: Color,
    textColor: Color,
    zoomScale: Float
) {
    val staffTop = startY + staffTopMargin
    val lineStroke = (1.4f * zoomScale).coerceAtLeast(1f)

    // 1. Draw 4 Red Staff Lines across the width
    for (i in 0..3) {
        val y = staffTop + (i * staffLineSpacing)
        drawLine(
            color = staffColor,
            start = Offset(6f, y),
            end = Offset(width - 6f, y),
            strokeWidth = lineStroke
        )
    }

    var cursorX = 14f * zoomScale

    // 2. Draw Clef at the beginning of each staff line
    drawClef(
        clef = line.clef,
        x = cursorX,
        staffTop = staffTop,
        staffLineSpacing = staffLineSpacing,
        color = noteColor,
        baseUnit = baseUnit
    )
    cursorX += baseUnit * 3.8f

    // 3. Draw Words, Syllables, Notes, and Bar lines
    val noteSize = baseUnit * 0.82f

    // Configure cached text paint
    val textColorArgb = textColor.toArgb()
    cachedTextPaint.color = textColorArgb
    cachedTextPaint.textSize = 13.5f * zoomScale

    cachedAccidentalPaint.color = noteColor.toArgb()
    cachedAccidentalPaint.textSize = noteSize * 1.5f

    for (word in line.words) {
        for (syllable in word.syllables) {
            val syllableStartX = cursorX

            // Draw notes/neumes in this syllable
            var noteCursorX = cursorX
            for (cluster in syllable.notes) {
                drawNeumeCluster(
                    cluster = cluster,
                    x = noteCursorX,
                    staffTop = staffTop,
                    staffLineSpacing = staffLineSpacing,
                    noteColor = noteColor,
                    noteSize = noteSize,
                    baseUnit = baseUnit
                )
                noteCursorX += max(cluster.notes.size * baseUnit * 1.1f, baseUnit * 1.3f)
            }

            // Draw Bar Line if present
            if (syllable.barLine != null) {
                drawBarLine(
                    barLine = syllable.barLine,
                    x = cursorX + (baseUnit * 0.8f),
                    staffTop = staffTop,
                    staffLineSpacing = staffLineSpacing,
                    color = noteColor,
                    strokeWidth = 2f * zoomScale
                )
                cursorX += baseUnit * 2.2f
            }

            // Draw Syllable Text below staff
            if (syllable.text.isNotEmpty()) {
                val textY = staffTop + (staffLineSpacing * 3) + (20f * zoomScale)
                drawContext.canvas.nativeCanvas.drawText(
                    syllable.text,
                    syllableStartX,
                    textY,
                    cachedTextPaint
                )
            }

            cursorX = max(noteCursorX, syllableStartX + (syllable.text.length * baseUnit * 0.82f)) + (baseUnit * 0.85f)
        }
        cursorX += baseUnit * 1.2f // Space between words
    }

    // 4. Draw Custos (guidon) at the right end of the staff line if next line exists
    if (line.nextLineFirstPitch != null) {
        val stepHeight = staffLineSpacing * 0.5f
        val custosY = staffTop + ((7 - line.nextLineFirstPitch) * stepHeight)
        val custosX = width - (18f * zoomScale)
        drawCustos(x = custosX, y = custosY, noteColor = noteColor, baseUnit = baseUnit)
    }
}

/**
 * Draws the traditional Solesmes Custos (guidon) at the end of a staff line.
 */
private fun DrawScope.drawCustos(
    x: Float,
    y: Float,
    noteColor: Color,
    baseUnit: Float
) {
    val h = baseUnit * 0.7f
    val w = baseUnit * 0.5f
    val path = Path().apply {
        moveTo(x, y)
        lineTo(x + w, y - (h * 0.6f))
        lineTo(x + w + 2f, y - (h * 0.6f))
        lineTo(x + 2f, y + 2f)
        close()
    }
    drawPath(path, color = noteColor, style = Fill)
    // Small vertical tail
    drawLine(
        color = noteColor,
        start = Offset(x + w, y - (h * 0.6f)),
        end = Offset(x + w, y - (h * 1.4f)),
        strokeWidth = 1.6f
    )
}

private fun DrawScope.drawClef(
    clef: GabcClef,
    x: Float,
    staffTop: Float,
    staffLineSpacing: Float,
    color: Color,
    baseUnit: Float
) {
    val clefY = staffTop + ((4 - clef.line) * staffLineSpacing)

    if (clef.type == ClefType.DO) {
        val halfW = baseUnit * 0.6f
        val blockH = staffLineSpacing * 0.9f

        // Upper block
        drawRect(
            color = color,
            topLeft = Offset(x, clefY - blockH),
            size = Size(halfW, blockH * 0.8f)
        )
        // Lower block
        drawRect(
            color = color,
            topLeft = Offset(x, clefY + (blockH * 0.2f)),
            size = Size(halfW, blockH * 0.8f)
        )
        // Connecting vertical bar
        drawLine(
            color = color,
            start = Offset(x + halfW, clefY - blockH),
            end = Offset(x + halfW, clefY + blockH),
            strokeWidth = 2.5f
        )
        // Left decorative prong
        drawLine(
            color = color,
            start = Offset(x - 3f, clefY - blockH + 3f),
            end = Offset(x, clefY - blockH),
            strokeWidth = 2f
        )
    } else {
        // Fa Clef
        drawCircle(color = color, radius = baseUnit * 0.4f, center = Offset(x + baseUnit * 0.4f, clefY))
        drawLine(
            color = color,
            start = Offset(x + baseUnit * 0.8f, clefY - staffLineSpacing),
            end = Offset(x + baseUnit * 0.8f, clefY + staffLineSpacing),
            strokeWidth = 2f
        )
    }
}

private fun DrawScope.drawNeumeCluster(
    cluster: GabcNoteCluster,
    x: Float,
    staffTop: Float,
    staffLineSpacing: Float,
    noteColor: Color,
    noteSize: Float,
    baseUnit: Float
) {
    var curX = x

    for (i in cluster.notes.indices) {
        val note = cluster.notes[i]
        val stepHeight = staffLineSpacing * 0.5f
        val noteY = staffTop + ((7 - note.pitchIndex) * stepHeight)

        if (note.isInclinatum) {
            // Diamond shape (Punctum Inclinatum)
            val path = Path().apply {
                moveTo(curX + noteSize / 2f, noteY - noteSize / 2f)
                lineTo(curX + noteSize, noteY)
                lineTo(curX + noteSize / 2f, noteY + noteSize / 2f)
                lineTo(curX, noteY)
                close()
            }
            drawPath(path, color = noteColor, style = Fill)
        } else if (note.isQuilisma) {
            // Wavy / serrated note
            val path = Path().apply {
                moveTo(curX, noteY - noteSize / 2f)
                lineTo(curX + noteSize * 0.3f, noteY - noteSize * 0.7f)
                lineTo(curX + noteSize * 0.6f, noteY - noteSize * 0.3f)
                lineTo(curX + noteSize, noteY - noteSize * 0.6f)
                lineTo(curX + noteSize, noteY + noteSize * 0.4f)
                lineTo(curX, noteY + noteSize * 0.4f)
                close()
            }
            drawPath(path, color = noteColor, style = Fill)
        } else {
            // Standard Punctum Quadratum (Square note)
            drawRect(
                color = noteColor,
                topLeft = Offset(curX, noteY - noteSize / 2f),
                size = Size(noteSize, noteSize)
            )

            // If Virga (note with right downward stem)
            if (note.isVirga) {
                drawLine(
                    color = noteColor,
                    start = Offset(curX + noteSize, noteY - noteSize / 2f),
                    end = Offset(curX + noteSize, noteY + staffLineSpacing * 1.2f),
                    strokeWidth = 2f
                )
            }
        }

        // Connecting stem for Podatus (ascending) or Clivis (descending)
        if (i < cluster.notes.size - 1) {
            val nextNote = cluster.notes[i + 1]
            val nextNoteY = staffTop + ((7 - nextNote.pitchIndex) * stepHeight)

            if (cluster.shape == NeumeShape.PODATUS) {
                drawLine(
                    color = noteColor,
                    start = Offset(curX + noteSize, noteY),
                    end = Offset(curX + noteSize, nextNoteY),
                    strokeWidth = 2f
                )
            } else if (cluster.shape == NeumeShape.CLIVIS) {
                drawLine(
                    color = noteColor,
                    start = Offset(curX, noteY),
                    end = Offset(curX, nextNoteY),
                    strokeWidth = 2f
                )
            }
        }

        // Punctum Mora (Dot '.')
        if (note.hasMora) {
            drawCircle(
                color = noteColor,
                radius = noteSize * 0.22f,
                center = Offset(curX + noteSize + (baseUnit * 0.35f), noteY)
            )
        }

        // Horizontal Episema ('_')
        if (note.hasEpisema) {
            drawLine(
                color = noteColor,
                start = Offset(curX - 2f, noteY - (noteSize * 0.8f)),
                end = Offset(curX + noteSize + 2f, noteY - (noteSize * 0.8f)),
                strokeWidth = 2f
            )
        }

        // Vertical Episema / Ictus ('\'')
        if (note.hasIctus) {
            drawLine(
                color = noteColor,
                start = Offset(curX + noteSize / 2f, noteY + (noteSize * 0.7f)),
                end = Offset(curX + noteSize / 2f, noteY + (noteSize * 1.3f)),
                strokeWidth = 1.8f
            )
        }

        // Accidental Flat ('x')
        if (note.isFlat) {
            drawContext.canvas.nativeCanvas.drawText(
                "♭",
                curX - (noteSize * 1.1f),
                noteY + (noteSize * 0.4f),
                cachedAccidentalPaint
            )
        }

        curX += noteSize * 1.15f
    }
}

private fun DrawScope.drawBarLine(
    barLine: BarLineType,
    x: Float,
    staffTop: Float,
    staffLineSpacing: Float,
    color: Color,
    strokeWidth: Float
) {
    val line1Y = staffTop + (staffLineSpacing * 3) // Bottom line
    val line2Y = staffTop + (staffLineSpacing * 2)
    val line3Y = staffTop + staffLineSpacing
    val line4Y = staffTop // Top line

    when (barLine) {
        BarLineType.VIRGULA -> {
            drawLine(
                color = color,
                start = Offset(x, line4Y - 4f),
                end = Offset(x + 4f, line4Y + 8f),
                strokeWidth = strokeWidth
            )
        }
        BarLineType.MINIMA -> {
            drawLine(
                color = color,
                start = Offset(x, line4Y),
                end = Offset(x, line3Y),
                strokeWidth = strokeWidth
            )
        }
        BarLineType.MINOR -> {
            drawLine(
                color = color,
                start = Offset(x, line3Y),
                end = Offset(x, line2Y),
                strokeWidth = strokeWidth
            )
        }
        BarLineType.MAIOR -> {
            drawLine(
                color = color,
                start = Offset(x, line4Y),
                end = Offset(x, line1Y),
                strokeWidth = strokeWidth
            )
        }
        BarLineType.FINALIS -> {
            drawLine(
                color = color,
                start = Offset(x - 3f, line4Y),
                end = Offset(x - 3f, line1Y),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = color,
                start = Offset(x + 3f, line4Y),
                end = Offset(x + 3f, line1Y),
                strokeWidth = strokeWidth * 1.8f
            )
        }
    }
}
