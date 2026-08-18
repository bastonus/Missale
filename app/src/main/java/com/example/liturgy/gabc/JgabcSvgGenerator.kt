package com.example.liturgy.gabc

import kotlin.math.max

/**
 * Generator producing clean, scalable SVG documents from parsed GABC scores,
 * following the jgabc / Gregorio vector layout standard.
 * Features:
 * - Red 4-line Gregorian staff lines (Solesmes rubric red #B71C1C).
 * - Vector Do / Fa clefs.
 * - Neumes (Punctum, Podatus, Clivis, Torculus, Porrectus, Quilisma, Custos).
 * - Rubric drop-caps and liturgical symbols (℣, ℟, *).
 * - Scriptural references and header metadata.
 */
object JgabcSvgGenerator {

    fun generateSvg(
        score: GabcScore,
        width: Float = 750f,
        staffLineSpacing: Float = 10f,
        staffColorHex: String = "#B71C1C",
        noteColorHex: String = "#000000",
        rubricColorHex: String = "#B71C1C",
        textColorHex: String = "#000000"
    ): String {
        val baseUnit = staffLineSpacing * 0.9f
        val systemHeight = staffLineSpacing * 7.5f
        val lines = layoutScoreIntoLines(score, width, baseUnit)
        val totalHeight = max(180f, lines.size * systemHeight + 60f)

        val sb = StringBuilder()
        sb.append("""<?xml version="1.0" encoding="UTF-8" standalone="no"?>""").append("\n")
        sb.append("""<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 $width $totalHeight" width="100%" height="100%">""").append("\n")
        sb.append("""  <style>""").append("\n")
        sb.append("""    .staff { stroke: $staffColorHex; stroke-width: 1.2; }""").append("\n")
        sb.append("""    .neume { fill: $noteColorHex; stroke: $noteColorHex; stroke-width: 0.5; }""").append("\n")
        sb.append("""    .text-lyrics { font-family: 'Times New Roman', Times, serif; font-size: 13px; fill: $textColorHex; }""").append("\n")
        sb.append("""    .text-rubric { font-family: 'Times New Roman', Times, serif; font-size: 13px; font-weight: bold; fill: $rubricColorHex; }""").append("\n")
        sb.append("""    .header-title { font-family: 'Times New Roman', Times, serif; font-size: 16px; font-weight: bold; fill: $rubricColorHex; }""").append("\n")
        sb.append("""    .header-meta { font-family: 'Times New Roman', Times, serif; font-size: 11px; font-style: italic; fill: #555555; }""").append("\n")
        sb.append("""  </style>""").append("\n")

        // Draw Metadata Header (propers_modern.html style)
        if (score.title.isNotEmpty() || score.officePart.isNotEmpty()) {
            val headerTitle = if (score.officePart.isNotEmpty()) "${score.officePart.uppercase()} — ${score.title}" else score.title
            sb.append("""  <text x="10" y="24" class="header-title">$headerTitle</text>""").append("\n")
            if (score.commentary.isNotEmpty() || score.mode.isNotEmpty()) {
                val metaText = listOfNotNull(
                    if (score.mode.isNotEmpty()) "T. ${score.mode}" else null,
                    score.commentary.ifEmpty { null },
                    score.book.ifEmpty { null }
                ).joinToString(" • ")
                sb.append("""  <text x="10" y="40" class="header-meta">$metaText</text>""").append("\n")
            }
        }

        val topOffset = if (score.title.isNotEmpty()) 55f else 15f

        // Draw each staff system line
        lines.forEachIndexed { lineIdx, line ->
            val startY = topOffset + (lineIdx * systemHeight)
            val staffTop = startY + (staffLineSpacing * 2f)

            // 1. Staff 4 lines
            for (i in 0..3) {
                val y = staffTop + (i * staffLineSpacing)
                sb.append("""  <line x1="10" y1="$y" x2="${width - 10}" y2="$y" class="staff"/>""").append("\n")
            }

            // 2. Clef
            val clefX = 18f
            sb.append(renderClefSvg(line.clef, clefX, staffTop, staffLineSpacing, baseUnit, noteColorHex))

            // 3. Syllables & Notes
            var currentX = clefX + (baseUnit * 3.8f)

            line.words.forEach { word ->
                word.syllables.forEach { syllable ->
                    val totalNotesInSyllable = syllable.notes.sumOf { it.notes.size }
                    val noteBlockWidth = max(totalNotesInSyllable * (baseUnit * 1.5f), baseUnit * 1.6f)
                    val barWidth = if (syllable.barLine != null) (baseUnit * 1.8f) else 0f
                    val textWidth = max(syllable.cleanText.length * baseUnit * 0.9f, baseUnit * 1.6f)
                    val syllableWidth = max(noteBlockWidth + barWidth, textWidth) + (baseUnit * 0.6f)

                    // Draw Notes
                    var noteX = currentX + (baseUnit * 0.2f)
                    syllable.notes.forEach { cluster ->
                        sb.append(renderClusterSvg(cluster, noteX, staffTop, staffLineSpacing, baseUnit, noteColorHex))
                        noteX += (baseUnit * 1.4f) * max(1, cluster.notes.size)
                    }

                    // Draw Bar Line
                    if (syllable.barLine != null) {
                        val barX = currentX + (syllableWidth / 2f)
                        sb.append(renderBarLineSvg(syllable.barLine, barX, staffTop, staffLineSpacing))
                    }

                    // Draw Lyrics
                    if (syllable.cleanText.isNotEmpty()) {
                        val lyricsY = staffTop + (3.8f * staffLineSpacing) + 14f
                        val isRubric = syllable.cleanText.startsWith("℣.") ||
                                syllable.cleanText.startsWith("℟.") ||
                                syllable.cleanText.startsWith("Ant.") ||
                                syllable.cleanText == "*"
                        val cssClass = if (isRubric) "text-rubric" else "text-lyrics"
                        val escapedText = escapeXml(syllable.cleanText)
                        sb.append("""  <text x="$currentX" y="$lyricsY" class="$cssClass">$escapedText</text>""").append("\n")
                    }

                    currentX += syllableWidth
                }
                currentX += (baseUnit * 0.8f)
            }

            // 4. Custos (Guidon at line end)
            if (line.nextLineFirstPitch != null) {
                val custosY = pitchToStaffY(line.nextLineFirstPitch, staffTop, staffLineSpacing)
                val custosX = width - 20f
                sb.append("""  <path d="M $custosX,$custosY l 4,-3 l 2,1 l -4,4 z" fill="$noteColorHex"/>""").append("\n")
            }
        }

        sb.append("</svg>")
        return sb.toString()
    }

    private fun renderClefSvg(
        clef: GabcClef,
        x: Float,
        staffTop: Float,
        spacing: Float,
        baseUnit: Float,
        colorHex: String
    ): String {
        val clefLineY = staffTop + ((4 - clef.line) * spacing)
        val w = baseUnit * 1.5f
        val h = spacing * 1.6f
        return if (clef.type == ClefType.DO) {
            """  <path d="M $x,${clefLineY - h/2} h $w v $h h -$w z M ${x + w*0.4f},${clefLineY - h*0.8f} v ${h*1.6f}" stroke="$colorHex" fill="$colorHex" stroke-width="1.2"/>""" + "\n"
        } else {
            """  <path d="M $x,${clefLineY - h/2} h $w v ${h*0.6f} h -$w z M ${x + w/2},${clefLineY - h/2} v ${h*1.2f}" stroke="$colorHex" fill="$colorHex" stroke-width="1.2"/>""" + "\n"
        }
    }

    private fun renderClusterSvg(
        cluster: GabcNoteCluster,
        x: Float,
        staffTop: Float,
        spacing: Float,
        baseUnit: Float,
        colorHex: String
    ): String {
        val sb = StringBuilder()
        val noteW = baseUnit * 1.3f
        val noteH = spacing * 0.85f

        cluster.notes.forEachIndexed { idx, note ->
            val noteY = pitchToStaffY(note.pitch, staffTop, spacing)
            val noteX = x + (idx * (baseUnit * 0.9f))

            if (note.isInclinatum) {
                // Diamond / Losange (Punctum inclinatum)
                val d = """M ${noteX + noteW/2},${noteY - noteH/2} l ${noteW/2},${noteH/2} l -${noteW/2},${noteH/2} l -${noteW/2},-${noteH/2} z"""
                sb.append("""  <path d="$d" fill="$colorHex"/>""").append("\n")
            } else {
                // Square (Punctum quadratum)
                sb.append("""  <rect x="$noteX" y="${noteY - noteH/2}" width="$noteW" height="$noteH" rx="1.5" fill="$colorHex"/>""").append("\n")
            }

            // Punctum mora (dot)
            if (note.hasMora) {
                val dotX = noteX + noteW + 3f
                sb.append("""  <circle cx="$dotX" cy="$noteY" r="1.8" fill="$colorHex"/>""").append("\n")
            }

            // Episema (horizontal line)
            if (note.hasEpisema) {
                sb.append("""  <line x1="$noteX" y1="${noteY - noteH/2 - 2.5f}" x2="${noteX + noteW}" y2="${noteY - noteH/2 - 2.5f}" stroke="$colorHex" stroke-width="1.5"/>""").append("\n")
            }
        }
        return sb.toString()
    }

    private fun renderBarLineSvg(barLine: BarLineType, x: Float, staffTop: Float, spacing: Float): String {
        return when (barLine) {
            BarLineType.VIRGULA -> """  <line x1="$x" y1="${staffTop - 2}" x2="${x + 3}" y2="${staffTop + spacing * 0.8f}" stroke="#000000" stroke-width="1.2"/>""" + "\n"
            BarLineType.MINIMA -> """  <line x1="$x" y1="$staffTop" x2="$x" y2="${staffTop + spacing}" stroke="#000000" stroke-width="1.2"/>""" + "\n"
            BarLineType.MINOR -> """  <line x1="$x" y1="${staffTop + spacing}" x2="$x" y2="${staffTop + spacing * 2}" stroke="#000000" stroke-width="1.2"/>""" + "\n"
            BarLineType.MAIOR -> """  <line x1="$x" y1="$staffTop" x2="$x" y2="${staffTop + spacing * 3}" stroke="#000000" stroke-width="1.2"/>""" + "\n"
            BarLineType.FINALIS -> """  <line x1="$x" y1="$staffTop" x2="$x" y2="${staffTop + spacing * 3}" stroke="#000000" stroke-width="1.2"/><line x1="${x + 3}" y1="$staffTop" x2="${x + 3}" y2="${staffTop + spacing * 3}" stroke="#000000" stroke-width="2.2"/>""" + "\n"
        }
    }

    private fun pitchToStaffY(pitch: Char, staffTop: Float, staffLineSpacing: Float): Float {
        val pitchStep = staffLineSpacing / 2f
        val line4Y = staffTop
        val index = (pitch.lowercaseChar() - 'a')
        // In GABC: 'c' is line 1, 'e' is line 2, 'g' is line 3, 'i' is line 4
        val stepsFromLine4 = ('i' - 'a') - index
        return line4Y + (stepsFromLine4 * pitchStep)
    }

    private fun layoutScoreIntoLines(score: GabcScore, width: Float, baseUnit: Float): List<ScoreStaffLine> {
        if (score.words.isEmpty()) return listOf(ScoreStaffLine(emptyList(), score.clef, null))
        val lines = mutableListOf<ScoreStaffLine>()
        val clefWidth = baseUnit * 4.5f
        val maxLineWidth = max(width - 40f, baseUnit * 25f)
        var currentWords = mutableListOf<GabcWord>()
        var currentLineWidth = clefWidth

        score.words.forEach { word ->
            val wordW = word.syllables.sumOf { syl ->
                val noteCount = syl.notes.sumOf { it.notes.size }
                val noteWidth = noteCount * (baseUnit * 1.4)
                val barWidth = if (syl.barLine != null) baseUnit * 2.0 else 0.0
                val textWidth = max(syl.cleanText.length * baseUnit * 0.9, baseUnit * 1.6)
                max(noteWidth + barWidth, textWidth) + (baseUnit * 0.6)
            }.toFloat()

            if (currentWords.isNotEmpty() && (currentLineWidth + wordW > maxLineWidth)) {
                lines.add(ScoreStaffLine(words = currentWords.toList(), clef = score.clef))
                currentWords = mutableListOf(word)
                currentLineWidth = clefWidth + wordW
            } else {
                currentWords.add(word)
                currentLineWidth += wordW + (baseUnit * 1.2f)
            }
        }

        if (currentWords.isNotEmpty()) {
            lines.add(ScoreStaffLine(words = currentWords.toList(), clef = score.clef))
        }

        return lines.mapIndexed { index, line ->
            val nextPitch = if (index < lines.size - 1) {
                lines[index + 1].words.firstOrNull()?.syllables?.firstOrNull()?.notes?.firstOrNull()?.notes?.firstOrNull()?.pitch
            } else null
            line.copy(nextLineFirstPitch = nextPitch)
        }
    }

    private fun escapeXml(str: String): String {
        return str.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}
