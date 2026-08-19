package com.example.liturgy.gabc

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

import com.example.ui.theme.GregorianStaffRed
import com.example.ui.theme.RubricRed

/**
 * Reusable cached paint objects to avoid heap allocation overhead during Canvas draw passes.
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
    val nextLineFirstPitch: Char? = null
)

/**
 * High-quality Jetpack Compose Composable for Gregorian Chant rendering using official Gregorio GABC standards.
 * Features:
 * 1. Gregorio GABC compliant rendering with full header parsing (name, office-part, mode, book, commentary).
 * 2. Official GABC source viewer & copy tool.
 * 3. Solesmes square notation on 4-line red/black staff.
 * 4. Multi-line wrapped systems with Custos (guidon).
 * 5. Horizontal continuous scroll mode.
 * 6. Pitch Pipe audio tone generator (Do, Re, Mi, Fa, Sol, La, Si).
 * 7. Dynamic staff color switcher (Rubric Red vs Black ink).
 * 8. Translation toggle & zoom scaling.
 */
@Composable
fun GregorianScoreView(
    rawGabc: String,
    title: String? = null,
    mode: String? = null,
    translation: String? = null,
    modifier: Modifier = Modifier,
    staffColor: Color = GregorianStaffRed,
    noteColor: Color = MaterialTheme.colorScheme.onSurface,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val score = remember(rawGabc) { GabcParser.parse(rawGabc) }
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var wrapLines by remember { mutableStateOf(true) }
    var useRedStaff by remember { mutableStateOf(true) }
    var useExsurgeEngine by remember { mutableStateOf(true) }
    var showTranslation by remember { mutableStateOf(true) }
    var showPitchMenu by remember { mutableStateOf(false) }
    var showGabcDialog by remember { mutableStateOf(false) }
    var showSvgDialog by remember { mutableStateOf(false) }
    var activePlayingNote by remember { mutableStateOf<String?>(null) }

    var showOptionsMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentStaffColor = if (useRedStaff) GregorianStaffRed else noteColor

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag("gregorian_score_card"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 2.dp)
        ) {
            // Header Facture: Solesmes modern_propers.html Style
            val displayTitle = title ?: score.title.ifEmpty { "Chant Grégorien" }
            val parts = displayTitle.split(" - ", limit = 2)
            val partName = if (parts.size == 2) parts[0] else score.officePart.ifEmpty { "PROPRIUM" }
            val chantTitle = if (parts.size == 2) parts[1] else displayTitle
            val displayMode = mode ?: score.mode
            val commentary = score.commentary.ifEmpty { score.book }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                // Top Meta Bar: Left (Mode/Office Tag), Center/Right (Commentary/Ref & Actions)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Tag: Part abbreviation & Mode numeral (e.g. Intr. VIII)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = RubricRed.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, RubricRed.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = partName.uppercase(),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = RubricRed
                            )
                        }
                        if (displayMode.isNotEmpty()) {
                            Text(
                                text = "Ton $displayMode",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Right: Actions (Pitch Pipe, Translation toggle, Menu)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Pitch Pipe Tone Generator
                        Box {
                            IconButton(
                                onClick = { showPitchMenu = !showPitchMenu },
                                modifier = Modifier.size(32.dp).testTag("pitch_pipe_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Donner le ton",
                                    tint = if (activePlayingNote != null) RubricRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showPitchMenu,
                                onDismissRequest = { showPitchMenu = false }
                            ) {
                                Text(
                                    text = "Donneur de ton (Pitch pipe)",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                PitchPlayer.notes.forEach { (name, freq) ->
                                    DropdownMenuItem(
                                        text = { Text(name) },
                                        onClick = {
                                            activePlayingNote = name
                                            PitchPlayer.playTone(freq, 2000) {
                                                activePlayingNote = null
                                            }
                                            showPitchMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        // Translation Toggle
                        if (!translation.isNullOrBlank()) {
                            IconButton(
                                onClick = { showTranslation = !showTranslation },
                                modifier = Modifier.size(32.dp).testTag("toggle_translation_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = "Afficher/masquer traduction",
                                    tint = if (showTranslation) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Consolidated Overflow Menu
                        Box {
                            IconButton(
                                onClick = { showOptionsMenu = !showOptionsMenu },
                                modifier = Modifier.size(32.dp).testTag("score_options_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Options de la partition",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showOptionsMenu,
                                onDismissRequest = { showOptionsMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Copier le code GABC") },
                                    leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                    onClick = {
                                        showOptionsMenu = false
                                        showGabcDialog = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Exporter en SVG") },
                                    leadingIcon = { Icon(Icons.Default.ViewStream, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                    onClick = {
                                        showOptionsMenu = false
                                        showSvgDialog = true
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(if (useRedStaff) "Portées noires" else "Portées rouges (Solesmes)") },
                                    leadingIcon = { Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                    onClick = {
                                        useRedStaff = !useRedStaff
                                        showOptionsMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Agrandir (+)") },
                                    leadingIcon = { Icon(Icons.Default.ZoomIn, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                    onClick = {
                                        zoomScale = (zoomScale + 0.15f).coerceAtMost(1.6f)
                                        showOptionsMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Réduire (-)") },
                                    leadingIcon = { Icon(Icons.Default.ZoomOut, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                    onClick = {
                                        zoomScale = (zoomScale - 0.15f).coerceAtLeast(0.75f)
                                        showOptionsMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Centered Chant Title & Biblical Reference (exact modern_propers.html layout)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = chantTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (commentary.isNotBlank()) {
                        Text(
                            text = commentary,
                            style = MaterialTheme.typography.labelSmall,
                            fontStyle = FontStyle.Italic,
                            color = RubricRed,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            val density = LocalDensity.current
            val baseUnit = 11.5f * zoomScale
            val staffLineSpacing = baseUnit * 1.05f
            val staffTopMargin = 32f * zoomScale
            val staffHeight = staffLineSpacing * 3
            val systemHeight = staffTopMargin + staffHeight + (32f * zoomScale)

            if (useExsurgeEngine) {
                // Exact Solesmes Gregorian SVG layout rendered via jgabc / exsurge engine
                ExsurgeChantView(
                    rawGabc = rawGabc,
                    useRedStaff = useRedStaff,
                    zoomScale = zoomScale,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
            } else {
                // Native Jetpack Compose Canvas renderer fallback
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
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
                                staffColor = currentStaffColor,
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
                                    staffColor = currentStaffColor,
                                    noteColor = noteColor,
                                    textColor = textColor,
                                    zoomScale = zoomScale
                                )
                            }
                        }
                    }
                }
            }

            // Translation / Vernacular commentary
            if (showTranslation && !translation.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = translation,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }

    // Official GABC Code Dialog
    if (showGabcDialog) {
        val scrollDialog = rememberScrollState()
        AlertDialog(
            onDismissRequest = { showGabcDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Code GABC officiel (Gregorio)")
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(scrollDialog)
                ) {
                    Text(
                        text = "Format standard officiel compatible avec Gregorio, GregoBase et jgabc :",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = rawGabc,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(rawGabc))
                        Toast.makeText(context, "Code GABC officiel copié !", Toast.LENGTH_SHORT).show()
                        showGabcDialog = false
                    }
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copier")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showGabcDialog = false }) {
                    Text("Fermer")
                }
            }
        )
    }

    // Official jgabc SVG Viewer & Exporter Dialog
    if (showSvgDialog) {
        val svgCode = remember(score, useRedStaff) {
            JgabcSvgGenerator.generateSvg(
                score = score,
                width = 750f,
                staffLineSpacing = 10f,
                staffColorHex = if (useRedStaff) "#B71C1C" else "#000000",
                noteColorHex = "#000000",
                rubricColorHex = "#B71C1C",
                textColorHex = "#000000"
            )
        }

        AlertDialog(
            onDismissRequest = { showSvgDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ViewStream, contentDescription = null, tint = RubricRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Code Vectoriel SVG (jgabc / Gregorio)")
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Format vectoriel SVG standard compatible jgabc, Inkscape, Illustrator et navigateurs web :",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = svgCode,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(svgCode))
                        Toast.makeText(context, "Code SVG jgabc copié !", Toast.LENGTH_SHORT).show()
                        showSvgDialog = false
                    }
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copier SVG")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSvgDialog = false }) {
                    Text("Fermer")
                }
            }
        )
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

    // Set custos target pitch for each line
    return lines.mapIndexed { index, line ->
        val nextPitch = if (index < lines.size - 1) {
            lines[index + 1].words.firstOrNull()?.syllables?.firstOrNull()?.notes?.firstOrNull()?.notes?.firstOrNull()?.pitch
        } else null
        line.copy(nextLineFirstPitch = nextPitch)
    }
}

private fun calculateWordWidth(word: GabcWord, baseUnit: Float): Float {
    return word.syllables.sumOf { syl ->
        val noteCount = syl.notes.sumOf { it.notes.size }
        val noteWidth = noteCount * (baseUnit * 1.4)
        val barWidth = if (syl.barLine != null) baseUnit * 2.0 else 0.0
        val textWidth = max(syl.cleanText.length * baseUnit * 0.9, baseUnit * 1.6)
        max(noteWidth + barWidth, textWidth) + (baseUnit * 0.6)
    }.toFloat()
}

private fun calculateScoreWidth(score: GabcScore, baseUnit: Float): Float {
    val clefWidth = baseUnit * 5.0f
    val wordsWidth = score.words.sumOf { calculateWordWidth(it, baseUnit).toDouble() }.toFloat()
    return clefWidth + wordsWidth + (baseUnit * 4f)
}

/**
 * Draws all wrapped lines sequentially down the Canvas.
 */
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
    lines.forEachIndexed { index, line ->
        val startY = index * systemHeight
        drawStaffLine(
            line = line,
            lineIndex = index,
            startY = startY,
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

/**
 * Renders a single 4-line staff system with its clef, neumes, lyrics, and optional Custos.
 */
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
    val staffLineWidth = 1.6f * zoomScale

    // 1. Draw 4 Red Staff Lines (portée à 4 lignes de Solesmes)
    for (i in 0..3) {
        val y = staffTop + (i * staffLineSpacing)
        drawLine(
            color = staffColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = staffLineWidth
        )
    }

    // 2. Draw Clef (Do clef / Fa clef)
    val clefX = baseUnit * 0.8f
    drawClef(line.clef, clefX, staffTop, staffLineSpacing, baseUnit, noteColor)

    // 3. Draw Words and Syllables
    var currentX = clefX + (baseUnit * 3.8f)

    for (word in line.words) {
        for (syllable in word.syllables) {
            val totalNotesInSyllable = syllable.notes.sumOf { it.notes.size }
            val noteBlockWidth = max(totalNotesInSyllable * (baseUnit * 1.5f), baseUnit * 1.6f)
            val barWidth = if (syllable.barLine != null) (baseUnit * 1.8f) else 0f
            val textWidth = max(syllable.cleanText.length * baseUnit * 0.9f, baseUnit * 1.6f)
            val syllableWidth = max(noteBlockWidth + barWidth, textWidth) + (baseUnit * 0.6f)

            // Draw Note Clusters
            var noteX = currentX + (baseUnit * 0.2f)
            for (cluster in syllable.notes) {
                drawGabcCluster(
                    cluster = cluster,
                    x = noteX,
                    staffTop = staffTop,
                    staffLineSpacing = staffLineSpacing,
                    baseUnit = baseUnit,
                    noteColor = noteColor
                )
                noteX += cluster.notes.size * (baseUnit * 1.45f)
            }

            // Draw Bar Line if any
            if (syllable.barLine != null) {
                drawBarLine(
                    barLine = syllable.barLine,
                    x = noteX + (baseUnit * 0.4f),
                    staffTop = staffTop,
                    staffLineSpacing = staffLineSpacing,
                    baseUnit = baseUnit,
                    color = noteColor
                )
            }

            // Draw Cleaned Lyrics / Syllable Text below staff
            if (syllable.cleanText.isNotEmpty()) {
                val lyricsY = staffTop + (3.8f * staffLineSpacing) + (16f * zoomScale)
                val isRubricSymbol = syllable.cleanText.startsWith("℣.") ||
                        syllable.cleanText.startsWith("℟.") ||
                        syllable.cleanText.startsWith("Ant.") ||
                        syllable.cleanText == "*" ||
                        syllable.cleanText.contains("ij.")
                val isInitialWordFirstLetter = lineIndex == 0 && currentX == clefX + (baseUnit * 3.8f)

                drawContext.canvas.nativeCanvas.apply {
                    cachedTextPaint.color = when {
                        isRubricSymbol -> RubricRed.toArgb()
                        isInitialWordFirstLetter -> RubricRed.toArgb()
                        else -> textColor.toArgb()
                    }
                    cachedTextPaint.isFakeBoldText = isRubricSymbol || isInitialWordFirstLetter
                    cachedTextPaint.textSize = if (isInitialWordFirstLetter) 16f * zoomScale else 14f * zoomScale
                    val drawX = currentX + max(0f, (syllableWidth - textWidth) / 4f)
                    drawText(syllable.cleanText, drawX, lyricsY, cachedTextPaint)
                    cachedTextPaint.isFakeBoldText = false
                }
            }

            currentX += syllableWidth
        }
        currentX += (baseUnit * 1.0f) // inter-word spacing
    }

    // 4. Draw Custos (Guidon) at end of line if there is a next line
    if (line.nextLineFirstPitch != null) {
        val custosX = width - (baseUnit * 2.2f)
        val custosY = getPitchY(line.nextLineFirstPitch, staffTop, staffLineSpacing)
        drawCustos(custosX, custosY, baseUnit, noteColor)
    }
}

/**
 * Calculates vertical Y coordinate for a Gregorian pitch (a..m on 4-line staff).
 * 'a' is below 1st line, 'c' is line 1, 'e' is line 2, 'g' is line 3, 'i' is line 4, etc.
 */
private fun getPitchY(pitch: Char, staffTop: Float, staffLineSpacing: Float): Float {
    val pitchIndex = (pitch.lowercaseChar() - 'a').coerceIn(0, 15)
    val bottomLineY = staffTop + (3 * staffLineSpacing)
    val halfSpace = staffLineSpacing / 2f
    // line 1 is pitch index 2 ('c')
    return bottomLineY - ((pitchIndex - 2) * halfSpace)
}

/**
 * Renders Gregorian Clefs (C-clef / Do or F-clef / Fa).
 */
private fun DrawScope.drawClef(
    clef: GabcClef,
    x: Float,
    staffTop: Float,
    staffLineSpacing: Float,
    baseUnit: Float,
    color: Color
) {
    val clefLineIndex = 4 - clef.line // 1-based from bottom
    val clefY = staffTop + (clefLineIndex * staffLineSpacing)

    if (clef.type == ClefType.DO) {
        // C-Clef: Classical Gregorian C-clef glyph
        val path = Path().apply {
            moveTo(x, clefY - (baseUnit * 0.7f))
            lineTo(x + (baseUnit * 0.8f), clefY - (baseUnit * 0.7f))
            lineTo(x + (baseUnit * 0.8f), clefY - (baseUnit * 0.2f))
            lineTo(x + (baseUnit * 0.4f), clefY)
            lineTo(x + (baseUnit * 0.8f), clefY + (baseUnit * 0.2f))
            lineTo(x + (baseUnit * 0.8f), clefY + (baseUnit * 0.7f))
            lineTo(x, clefY + (baseUnit * 0.7f))
            close()
        }
        drawPath(path, color)
        drawLine(
            color = color,
            start = Offset(x + (baseUnit * 0.4f), clefY - (baseUnit * 1.2f)),
            end = Offset(x + (baseUnit * 0.4f), clefY + (baseUnit * 1.2f)),
            strokeWidth = baseUnit * 0.35f
        )
    } else {
        // F-Clef
        drawCircle(
            color = color,
            radius = baseUnit * 0.45f,
            center = Offset(x + (baseUnit * 0.5f), clefY)
        )
        drawLine(
            color = color,
            start = Offset(x + (baseUnit * 0.5f), clefY - (baseUnit * 0.8f)),
            end = Offset(x + (baseUnit * 0.5f), clefY + (baseUnit * 0.8f)),
            strokeWidth = baseUnit * 0.3f
        )
    }
}

/**
 * Draws a note cluster (Punctum, Podatus, Clivis, Torculus, Porrectus, etc.).
 */
private fun DrawScope.drawGabcCluster(
    cluster: GabcNoteCluster,
    x: Float,
    staffTop: Float,
    staffLineSpacing: Float,
    baseUnit: Float,
    noteColor: Color
) {
    when (cluster.shape) {
        NeumeShape.PODATUS -> {
            if (cluster.notes.size >= 2) {
                val lowerNote = cluster.notes[0]
                val upperNote = cluster.notes[1]
                val lowerY = getPitchY(lowerNote.pitch, staffTop, staffLineSpacing)
                val upperY = getPitchY(upperNote.pitch, staffTop, staffLineSpacing)
                val noteSize = baseUnit * 0.95f

                drawRect(color = noteColor, topLeft = Offset(x, lowerY - (noteSize / 2f)), size = Size(noteSize, noteSize))
                drawRect(color = noteColor, topLeft = Offset(x, upperY - (noteSize / 2f)), size = Size(noteSize, noteSize))
                drawLine(
                    color = noteColor,
                    start = Offset(x + noteSize, upperY),
                    end = Offset(x + noteSize, lowerY),
                    strokeWidth = baseUnit * 0.22f
                )
                drawDecorations(lowerNote, x, lowerY, baseUnit, noteColor)
                drawDecorations(upperNote, x, upperY, baseUnit, noteColor)
                return
            }
        }
        NeumeShape.CLIVIS -> {
            if (cluster.notes.size >= 2) {
                val firstNote = cluster.notes[0]
                val secondNote = cluster.notes[1]
                val firstY = getPitchY(firstNote.pitch, staffTop, staffLineSpacing)
                val secondY = getPitchY(secondNote.pitch, staffTop, staffLineSpacing)
                val noteSize = baseUnit * 0.95f

                drawRect(color = noteColor, topLeft = Offset(x, firstY - (noteSize / 2f)), size = Size(noteSize, noteSize))
                drawRect(color = noteColor, topLeft = Offset(x + (noteSize * 1.1f), secondY - (noteSize / 2f)), size = Size(noteSize, noteSize))
                drawLine(
                    color = noteColor,
                    start = Offset(x, firstY),
                    end = Offset(x, secondY),
                    strokeWidth = baseUnit * 0.22f
                )
                drawDecorations(firstNote, x, firstY, baseUnit, noteColor)
                drawDecorations(secondNote, x + (noteSize * 1.1f), secondY, baseUnit, noteColor)
                return
            }
        }
        NeumeShape.PORRECTUS -> {
            if (cluster.notes.size >= 3) {
                val n1 = cluster.notes[0]
                val n2 = cluster.notes[1]
                val n3 = cluster.notes[2]
                val y1 = getPitchY(n1.pitch, staffTop, staffLineSpacing)
                val y2 = getPitchY(n2.pitch, staffTop, staffLineSpacing)
                val y3 = getPitchY(n3.pitch, staffTop, staffLineSpacing)
                val noteSize = baseUnit * 0.95f

                // Diagonal bar
                val path = Path().apply {
                    moveTo(x, y1 - (noteSize / 2f))
                    lineTo(x + (noteSize * 1.8f), y2 - (noteSize / 2f))
                    lineTo(x + (noteSize * 1.8f), y2 + (noteSize / 2f))
                    lineTo(x, y1 + (noteSize / 2f))
                    close()
                }
                drawPath(path, noteColor)
                drawRect(color = noteColor, topLeft = Offset(x + (noteSize * 1.8f), y3 - (noteSize / 2f)), size = Size(noteSize, noteSize))
                drawLine(
                    color = noteColor,
                    start = Offset(x + (noteSize * 1.8f), y2),
                    end = Offset(x + (noteSize * 1.8f), y3),
                    strokeWidth = baseUnit * 0.22f
                )
                return
            }
        }
        else -> {}
    }

    // Default: draw individual notes
    var currentNoteX = x
    for (note in cluster.notes) {
        drawGabcNote(note, currentNoteX, staffTop, staffLineSpacing, baseUnit, noteColor)
        currentNoteX += (baseUnit * 1.35f)
    }
}

private fun DrawScope.drawGabcNote(
    note: GabcNote,
    x: Float,
    staffTop: Float,
    staffLineSpacing: Float,
    baseUnit: Float,
    noteColor: Color
) {
    val y = getPitchY(note.pitch, staffTop, staffLineSpacing)
    val noteSize = baseUnit * 0.95f

    when {
        note.isInclinatum -> {
            // Diamond / Lozenge note
            val path = Path().apply {
                moveTo(x + (noteSize / 2f), y - (noteSize * 0.55f))
                lineTo(x + noteSize, y)
                lineTo(x + (noteSize / 2f), y + (noteSize * 0.55f))
                lineTo(x, y)
                close()
            }
            drawPath(path, noteColor)
        }
        note.isQuilisma -> {
            drawRect(
                color = noteColor,
                topLeft = Offset(x, y - (noteSize / 2f)),
                size = Size(noteSize, noteSize),
                style = Fill
            )
            drawCircle(
                color = Color.White,
                radius = noteSize * 0.2f,
                center = Offset(x + (noteSize * 0.3f), y)
            )
        }
        note.isVirga -> {
            drawRect(
                color = noteColor,
                topLeft = Offset(x, y - (noteSize / 2f)),
                size = Size(noteSize, noteSize)
            )
            drawLine(
                color = noteColor,
                start = Offset(x + noteSize, y - (noteSize / 2f)),
                end = Offset(x + noteSize, y + (noteSize * 1.6f)),
                strokeWidth = baseUnit * 0.22f
            )
        }
        else -> {
            drawRect(
                color = noteColor,
                topLeft = Offset(x, y - (noteSize / 2f)),
                size = Size(noteSize, noteSize)
            )
        }
    }

    drawDecorations(note, x, y, baseUnit, noteColor)
}

private fun DrawScope.drawDecorations(
    note: GabcNote,
    x: Float,
    y: Float,
    baseUnit: Float,
    noteColor: Color
) {
    val noteSize = baseUnit * 0.95f

    // Punctum Mora
    if (note.hasMora) {
        drawCircle(
            color = noteColor,
            radius = baseUnit * 0.22f,
            center = Offset(x + noteSize + (baseUnit * 0.45f), y)
        )
    }

    // Horizontal Episema
    if (note.hasEpisema) {
        drawLine(
            color = noteColor,
            start = Offset(x - (baseUnit * 0.1f), y - (noteSize * 0.85f)),
            end = Offset(x + noteSize + (baseUnit * 0.1f), y - (noteSize * 0.85f)),
            strokeWidth = baseUnit * 0.2f
        )
    }

    // Vertical Episema (Ictus)
    if (note.hasIctus) {
        drawLine(
            color = noteColor,
            start = Offset(x + (noteSize / 2f), y + (noteSize * 0.6f)),
            end = Offset(x + (noteSize / 2f), y + (noteSize * 1.2f)),
            strokeWidth = baseUnit * 0.2f
        )
    }

    // Accidental (Flat / Natural / Sharp)
    if (note.isFlat || note.isNatural || note.isSharp) {
        drawContext.canvas.nativeCanvas.apply {
            cachedAccidentalPaint.color = noteColor.toArgb()
            cachedAccidentalPaint.textSize = baseUnit * 1.5f
            val accidentalChar = when {
                note.isFlat -> "♭"
                note.isNatural -> "♮"
                else -> "♯"
            }
            drawText(accidentalChar, x - (baseUnit * 1.2f), y + (baseUnit * 0.4f), cachedAccidentalPaint)
        }
    }
}

private fun DrawScope.drawBarLine(
    barLine: BarLineType,
    x: Float,
    staffTop: Float,
    staffLineSpacing: Float,
    baseUnit: Float,
    color: Color
) {
    when (barLine) {
        BarLineType.VIRGULA -> {
            drawLine(
                color = color,
                start = Offset(x, staffTop),
                end = Offset(x, staffTop + (staffLineSpacing * 0.6f)),
                strokeWidth = baseUnit * 0.25f
            )
        }
        BarLineType.MINIMA -> {
            drawLine(
                color = color,
                start = Offset(x, staffTop),
                end = Offset(x, staffTop + staffLineSpacing),
                strokeWidth = baseUnit * 0.25f
            )
        }
        BarLineType.MINOR -> {
            drawLine(
                color = color,
                start = Offset(x, staffTop + staffLineSpacing),
                end = Offset(x, staffTop + (staffLineSpacing * 2f)),
                strokeWidth = baseUnit * 0.25f
            )
        }
        BarLineType.MAIOR -> {
            drawLine(
                color = color,
                start = Offset(x, staffTop),
                end = Offset(x, staffTop + (staffLineSpacing * 3f)),
                strokeWidth = baseUnit * 0.25f
            )
        }
        BarLineType.FINALIS -> {
            drawLine(
                color = color,
                start = Offset(x, staffTop),
                end = Offset(x, staffTop + (staffLineSpacing * 3f)),
                strokeWidth = baseUnit * 0.25f
            )
            drawLine(
                color = color,
                start = Offset(x + (baseUnit * 0.5f), staffTop),
                end = Offset(x + (baseUnit * 0.5f), staffTop + (staffLineSpacing * 3f)),
                strokeWidth = baseUnit * 0.45f
            )
        }
    }
}

/**
 * Draws the Custos (Guidon) indicating the starting pitch of the next line.
 */
private fun DrawScope.drawCustos(
    x: Float,
    y: Float,
    baseUnit: Float,
    color: Color
) {
    val size = baseUnit * 0.55f
    val path = Path().apply {
        moveTo(x, y)
        lineTo(x + size, y - (size * 0.6f))
        lineTo(x + (size * 1.8f), y)
        lineTo(x + size, y + (size * 0.6f))
        close()
    }
    drawPath(path, color)
    drawLine(
        color = color,
        start = Offset(x + size, y - (size * 0.6f)),
        end = Offset(x + size, y - (size * 1.6f)),
        strokeWidth = baseUnit * 0.2f
    )
}
