package com.example.liturgy.gabc

/**
 * Data structures for parsed official GABC chant notation (Gregorio / GregoBase format).
 */
data class GabcScore(
    val title: String = "",
    val officePart: String = "",
    val mode: String = "",
    val book: String = "",
    val transcriber: String = "",
    val commentary: String = "",
    val rawHeader: String = "",
    val rawGabc: String = "",
    val clef: GabcClef = GabcClef(ClefType.DO, 4),
    val words: List<GabcWord> = emptyList(),
    val initialLetter: String = ""
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
    val cleanText: String,
    val notes: List<GabcNoteCluster>,
    val barLine: BarLineType? = null,
    val isLineBreak: Boolean = false
)

data class GabcNoteCluster(
    val notes: List<GabcNote>,
    val shape: NeumeShape = NeumeShape.PUNCTUM
)

data class GabcNote(
    val pitch: Char, // 'a' to 'm', 'n', 'p'
    val pitchIndex: Int, // 0 for 'a', 1 for 'b', 2 for 'c' (line 1), etc.
    val hasMora: Boolean = false, // '.'
    val hasIctus: Boolean = false, // '\''
    val hasEpisema: Boolean = false, // '_'
    val isQuilisma: Boolean = false, // 'w'
    val isVirga: Boolean = false, // 'v'
    val isInclinatum: Boolean = false, // 'q' or diamond
    val isStropha: Boolean = false, // 's'
    val isOriscus: Boolean = false, // 'o'
    val isLiquescent: Boolean = false, // '~'
    val isFlat: Boolean = false, // 'x'
    val isNatural: Boolean = false, // 'y'
    val isSharp: Boolean = false // '#'
)

enum class NeumeShape {
    PUNCTUM,
    VIRGA,
    PUNCTUM_INCLINATUM,
    PODATUS, // 2 notes ascending
    CLIVIS, // 2 notes descending
    TORCULUS, // 3 notes: low-high-low
    PORRECTUS, // 3 notes: high-low-high
    SCANDICUS, // 3 notes ascending
    CLIMACUS, // descending with puncta inclinata
    COMPOUND
}

enum class BarLineType {
    VIRGULA, // ' or `
    MINIMA, // ,
    MINOR, // ;
    MAIOR, // :
    FINALIS // ::
}

/**
 * Parser for official GABC notation strings according to the Gregorio standard.
 */
object GabcParser {

    fun parse(rawGabc: String): GabcScore {
        var title = ""
        var officePart = ""
        var mode = ""
        var book = ""
        var transcriber = ""
        var commentary = ""
        var rawHeader = ""
        var clef = GabcClef(ClefType.DO, 4)
        var notationBody = rawGabc

        // 1. Parse official GABC header (separated by %%)
        if (rawGabc.contains("%%")) {
            val parts = rawGabc.split("%%", limit = 2)
            rawHeader = parts[0].trim()
            val headerLines = parts[0].lines()
            for (line in headerLines) {
                val trimmed = line.trim()
                when {
                    trimmed.startsWith("name:", ignoreCase = true) -> {
                        title = trimmed.substring(5).trim().trimEnd(';')
                    }
                    trimmed.startsWith("office-part:", ignoreCase = true) -> {
                        officePart = trimmed.substring(12).trim().trimEnd(';')
                    }
                    trimmed.startsWith("mode:", ignoreCase = true) -> {
                        mode = trimmed.substring(5).trim().trimEnd(';')
                    }
                    trimmed.startsWith("book:", ignoreCase = true) -> {
                        book = trimmed.substring(5).trim().trimEnd(';')
                    }
                    trimmed.startsWith("transcriber:", ignoreCase = true) -> {
                        transcriber = trimmed.substring(12).trim().trimEnd(';')
                    }
                    trimmed.startsWith("commentary:", ignoreCase = true) -> {
                        commentary = trimmed.substring(11).trim().trimEnd(';')
                    }
                }
            }
            notationBody = parts[1]
        }

        // 2. Extract initial clef from notation (e.g. (c1), (c2), (c3), (c4), (cb3), (cb4), (f1), (f2), (f3), (f4))
        val clefRegex = Regex("""\(([cf]b?[1-4])\)""")
        val clefMatch = clefRegex.find(notationBody)
        if (clefMatch != null) {
            val clefStr = clefMatch.groupValues[1].lowercase()
            val type = if (clefStr.startsWith("f")) ClefType.FA else ClefType.DO
            val lineDigit = clefStr.filter { it.isDigit() }.toIntOrNull() ?: 4
            clef = GabcClef(type, lineDigit)
            notationBody = notationBody.replaceFirst(clefMatch.value, "")
        }

        // 3. Parse syllables, words, line breaks, and bar lines
        val syllableRegex = Regex("""([^\(\)\s]*)\(([^)]*)\)""")
        val matches = syllableRegex.findAll(notationBody).toList()

        val words = mutableListOf<GabcWord>()
        var currentSyllables = mutableListOf<GabcSyllable>()
        var initialLetter = ""

        for (match in matches) {
            val rawLyric = match.groupValues[1].trim()
            val notesStr = match.groupValues[2].trim()

            val isLineBreak = notesStr.equals("z", ignoreCase = true) || notesStr.equals("Z", ignoreCase = true)
            val barLine = parseBarLine(notesStr)
            val clusters = parseNoteClusters(notesStr)
            val cleanLyric = cleanGabcLyric(rawLyric)

            if (initialLetter.isEmpty() && cleanLyric.isNotEmpty() && cleanLyric.first().isLetter()) {
                initialLetter = cleanLyric.first().toString()
            }

            val syllable = GabcSyllable(
                text = rawLyric,
                cleanText = cleanLyric,
                notes = clusters,
                barLine = barLine,
                isLineBreak = isLineBreak
            )
            currentSyllables.add(syllable)

            // Word separation heuristics
            val isEndWord = rawLyric.isNotEmpty() || barLine != null || isLineBreak ||
                    (match.range.last < notationBody.length - 1 && notationBody[match.range.last + 1].isWhitespace())

            if (isEndWord) {
                words.add(GabcWord(currentSyllables.toList()))
                currentSyllables = mutableListOf()
            }
        }

        if (currentSyllables.isNotEmpty()) {
            words.add(GabcWord(currentSyllables.toList()))
        }

        return GabcScore(
            title = title,
            officePart = officePart,
            mode = mode,
            book = book,
            transcriber = transcriber,
            commentary = commentary,
            rawHeader = rawHeader,
            rawGabc = rawGabc,
            clef = clef,
            words = words,
            initialLetter = initialLetter
        )
    }

    /**
     * Converts standard Gregorio GABC tags into clean unicode representation for screen display.
     */
    fun cleanGabcLyric(lyric: String): String {
        var res = lyric
        res = res.replace("<sp>V/</sp>", "℣. ")
        res = res.replace("<sp>V</sp>", "℣. ")
        res = res.replace("<sp>R/</sp>", "℟. ")
        res = res.replace("<sp>R</sp>", "℟. ")
        res = res.replace("<sp>A/</sp>", "Ant. ")
        res = res.replace("<sp>ae</sp>", "æ")
        res = res.replace("<sp>'ae</sp>", "ǽ")
        res = res.replace("<sp>oe</sp>", "œ")
        res = res.replace("<sp>'oe</sp>", "œ́")
        res = res.replace("<i>", "")
        res = res.replace("</i>", "")
        res = res.replace("<b>", "")
        res = res.replace("</b>", "")
        res = res.replace("<c>", "")
        res = res.replace("</c>", "")
        res = res.replace("<nl>", "\n")
        return res
    }

    private fun parseBarLine(notesStr: String): BarLineType? {
        return when (notesStr) {
            "::" -> BarLineType.FINALIS
            ":" -> BarLineType.MAIOR
            ";" -> BarLineType.MINOR
            "," -> BarLineType.MINIMA
            "`", "'" -> BarLineType.VIRGULA
            else -> null
        }
    }

    private fun parseNoteClusters(notesStr: String): List<GabcNoteCluster> {
        if (notesStr.isEmpty() || parseBarLine(notesStr) != null || notesStr.equals("z", ignoreCase = true) || notesStr.equals("Z", ignoreCase = true)) {
            return emptyList()
        }

        val clusters = mutableListOf<GabcNoteCluster>()
        // Split sub-clusters separated by '/' or '!'
        val subParts = notesStr.split(Regex("[/!]")).filter { it.isNotEmpty() }

        for (part in subParts) {
            val parsedNotes = mutableListOf<GabcNote>()
            var i = 0
            while (i < part.length) {
                val char = part[i]
                if (char.lowercaseChar() in 'a'..'p') {
                    val pitch = char.lowercaseChar()
                    val pitchIndex = pitch - 'a'

                    var hasMora = false
                    var hasIctus = false
                    var hasEpisema = false
                    var isQuilisma = false
                    var isVirga = false
                    var isInclinatum = false
                    var isStropha = false
                    var isOriscus = false
                    var isLiquescent = false
                    var isFlat = false
                    var isNatural = false
                    var isSharp = false

                    var j = i + 1
                    while (j < part.length && (part[j] in "._'wvqso~xy#<>" || !part[j].isLetter())) {
                        when (part[j]) {
                            '.' -> hasMora = true
                            '\'' -> hasIctus = true
                            '_' -> hasEpisema = true
                            'w' -> isQuilisma = true
                            'v' -> isVirga = true
                            'q' -> isInclinatum = true
                            's' -> isStropha = true
                            'o' -> isOriscus = true
                            '~', '<', '>' -> isLiquescent = true
                            'x' -> isFlat = true
                            'y' -> isNatural = true
                            '#' -> isSharp = true
                        }
                        if (part[j] in "._'wvqso~xy#<>") {
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
                            isStropha = isStropha,
                            isOriscus = isOriscus,
                            isLiquescent = isLiquescent,
                            isFlat = isFlat,
                            isNatural = isNatural,
                            isSharp = isSharp
                        )
                    )
                    i = j - 1
                }
                i++
            }

            if (parsedNotes.isNotEmpty()) {
                val shape = determineClusterShape(parsedNotes)
                clusters.add(GabcNoteCluster(parsedNotes, shape))
            }
        }

        return clusters
    }

    private fun determineClusterShape(notes: List<GabcNote>): NeumeShape {
        return when (notes.size) {
            1 -> {
                if (notes[0].isVirga) NeumeShape.VIRGA
                else if (notes[0].isInclinatum) NeumeShape.PUNCTUM_INCLINATUM
                else NeumeShape.PUNCTUM
            }
            2 -> {
                if (notes[0].pitchIndex < notes[1].pitchIndex) NeumeShape.PODATUS
                else NeumeShape.CLIVIS
            }
            3 -> {
                val p0 = notes[0].pitchIndex
                val p1 = notes[1].pitchIndex
                val p2 = notes[2].pitchIndex
                if (p0 < p1 && p1 > p2) NeumeShape.TORCULUS
                else if (p0 > p1 && p1 < p2) NeumeShape.PORRECTUS
                else if (p0 < p1 && p1 < p2) NeumeShape.SCANDICUS
                else if (p0 > p1 && p1 > p2) NeumeShape.CLIMACUS
                else NeumeShape.COMPOUND
            }
            else -> NeumeShape.COMPOUND
        }
    }
}
