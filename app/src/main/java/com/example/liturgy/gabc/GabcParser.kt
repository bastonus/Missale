package com.example.liturgy.gabc

/**
 * Data structures for parsed GABC chant notation.
 */
data class GabcScore(
    val title: String = "",
    val mode: String = "",
    val clef: GabcClef = GabcClef(ClefType.DO, 4),
    val words: List<GabcWord> = emptyList()
)

data class GabcClef(
    val type: ClefType,
    val line: Int // 1 (bottom) to 4 (top)
)

enum class ClefType {
    DO, FA
}

data class GabcWord(
    val syllables: List<GabcSyllable>
)

data class GabcSyllable(
    val text: String,
    val notes: List<GabcNoteCluster>,
    val barLine: BarLineType? = null
)

data class GabcNoteCluster(
    val notes: List<GabcNote>,
    val shape: NeumeShape = NeumeShape.PUNCTUM
)

data class GabcNote(
    val pitch: Char, // 'a' to 'm'
    val pitchIndex: Int, // 0 for 'a', 1 for 'b' (line 1), etc.
    val hasMora: Boolean = false, // '.'
    val hasIctus: Boolean = false, // '\''
    val hasEpisema: Boolean = false, // '_'
    val isQuilisma: Boolean = false, // 'w'
    val isVirga: Boolean = false, // 'v'
    val isInclinatum: Boolean = false, // 'q' or diamond in descending series
    val isFlat: Boolean = false, // 'x'
    val isNatural: Boolean = false // 'y'
)

enum class NeumeShape {
    PUNCTUM,
    PODATUS, // 2 notes ascending
    CLIVIS, // 2 notes descending
    TORCULUS, // 3 notes: low-high-low
    PORRECTUS, // 3 notes: high-low-high
    COMPOUND
}

enum class BarLineType {
    VIRGULA, // '
    MINIMA, // ,
    MINOR, // ;
    MAIOR, // :
    FINALIS // ::
}

/**
 * Parser for GABC notation strings (as used in Gregorio / jgabc).
 */
object GabcParser {

    fun parse(rawGabc: String): GabcScore {
        var title = ""
        var mode = ""
        var clef = GabcClef(ClefType.DO, 4)
        var notationBody = rawGabc

        // 1. Check for header (separated by %%)
        if (rawGabc.contains("%%")) {
            val parts = rawGabc.split("%%", limit = 2)
            val headerLines = parts[0].lines()
            for (line in headerLines) {
                val trimmed = line.trim()
                if (trimmed.startsWith("name:")) {
                    title = trimmed.removePrefix("name:").trim().trimEnd(';')
                } else if (trimmed.startsWith("mode:")) {
                    mode = trimmed.removePrefix("mode:").trim().trimEnd(';')
                }
            }
            notationBody = parts[1]
        }

        // 2. Extract initial clef from notation (e.g. (c4), (c3), (f3), (f4))
        val clefRegex = Regex("""\(([cf][1-4])\)""")
        val clefMatch = clefRegex.find(notationBody)
        if (clefMatch != null) {
            val clefStr = clefMatch.groupValues[1]
            val type = if (clefStr.startsWith("f", ignoreCase = true)) ClefType.FA else ClefType.DO
            val line = clefStr.substring(1).toIntOrNull() ?: 4
            clef = GabcClef(type, line)
            // Remove the clef tag from body for syllable parsing
            notationBody = notationBody.replaceFirst(clefMatch.value, "")
        }

        // 3. Parse syllables and words
        // Pattern matches: LyricText(notes/bars)
        val syllableRegex = Regex("""([^\(\)\s]*)\(([^)]*)\)""")
        val matches = syllableRegex.findAll(notationBody).toList()

        val words = mutableListOf<GabcWord>()
        var currentSyllables = mutableListOf<GabcSyllable>()

        for (match in matches) {
            val lyric = match.groupValues[1].trim()
            val notesStr = match.groupValues[2].trim()

            val barLine = parseBarLine(notesStr)
            val clusters = parseNoteClusters(notesStr)

            val syllable = GabcSyllable(
                text = lyric,
                notes = clusters,
                barLine = barLine
            )
            currentSyllables.add(syllable)

            // If lyric ends or has spacing, consider word separation
            if (lyric.isNotEmpty() || barLine != null || match.range.last < notationBody.length - 1 && notationBody[match.range.last + 1].isWhitespace()) {
                words.add(GabcWord(currentSyllables.toList()))
                currentSyllables = mutableListOf()
            }
        }

        if (currentSyllables.isNotEmpty()) {
            words.add(GabcWord(currentSyllables.toList()))
        }

        return GabcScore(
            title = title,
            mode = mode,
            clef = clef,
            words = words
        )
    }

    private fun parseBarLine(notesStr: String): BarLineType? {
        return when {
            notesStr == "::" -> BarLineType.FINALIS
            notesStr == ":" -> BarLineType.MAIOR
            notesStr == ";" -> BarLineType.MINOR
            notesStr == "," -> BarLineType.MINIMA
            notesStr == "`" || notesStr == "'" -> BarLineType.VIRGULA
            else -> null
        }
    }

    private fun parseNoteClusters(notesStr: String): List<GabcNoteCluster> {
        if (notesStr.isEmpty() || parseBarLine(notesStr) != null) return emptyList()

        val clusters = mutableListOf<GabcNoteCluster>()
        val parsedNotes = mutableListOf<GabcNote>()

        var i = 0
        while (i < notesStr.length) {
            val char = notesStr[i]

            // Check if pitch letter (a to m)
            if (char.lowercaseChar() in 'a'..'m') {
                val pitch = char.lowercaseChar()
                val pitchIndex = pitch - 'a'

                var hasMora = false
                var hasIctus = false
                var hasEpisema = false
                var isQuilisma = false
                var isVirga = false
                var isInclinatum = false
                var isFlat = false
                var isNatural = false

                // Inspect modifiers following note
                var j = i + 1
                while (j < notesStr.length && !notesStr[j].lowercaseChar().isLetter() || (j < notesStr.length && notesStr[j] in "._'wvqxy")) {
                    when (notesStr[j]) {
                        '.' -> hasMora = true
                        '\'' -> hasIctus = true
                        '_' -> hasEpisema = true
                        'w' -> isQuilisma = true
                        'v' -> isVirga = true
                        'q' -> isInclinatum = true
                        'x' -> isFlat = true
                        'y' -> isNatural = true
                    }
                    if (notesStr[j] in "._'wvqxy") {
                        j++
                    } else {
                        break
                    }
                }

                parsedNotes.add(
                    GabcNote(
                        pitch = pitch,
                        pitchIndex = pitchIndex,
                        hasMora = hasMora,
                        hasIctus = hasIctus,
                        hasEpisema = hasEpisema,
                        isQuilisma = isQuilisma,
                        isVirga = isVirga,
                        isInclinatum = isInclinatum,
                        isFlat = isFlat,
                        isNatural = isNatural
                    )
                )
                i = j - 1
            }
            i++
        }

        if (parsedNotes.isNotEmpty()) {
            val shape = when (parsedNotes.size) {
                1 -> NeumeShape.PUNCTUM
                2 -> if (parsedNotes[0].pitchIndex < parsedNotes[1].pitchIndex) NeumeShape.PODATUS else NeumeShape.CLIVIS
                3 -> {
                    val p0 = parsedNotes[0].pitchIndex
                    val p1 = parsedNotes[1].pitchIndex
                    val p2 = parsedNotes[2].pitchIndex
                    if (p0 < p1 && p1 > p2) NeumeShape.TORCULUS
                    else if (p0 > p1 && p1 < p2) NeumeShape.PORRECTUS
                    else NeumeShape.COMPOUND
                }
                else -> NeumeShape.COMPOUND
            }
            clusters.add(GabcNoteCluster(parsedNotes, shape))
        }

        return clusters
    }
}
