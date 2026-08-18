package com.example.liturgy.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

/**
 * Liturgical Seasons according to the 1960 Code of Rubrics (Missale & Breviarium Romanum 1960).
 */
enum class LiturgicalSeason(val latinName: String, val frenchName: String, val defaultColor: LiturgicalColor) {
    ADVENT("Tempus Adventus", "Temps de l'Avent", LiturgicalColor.VIOLET),
    CHRISTMASTIDE("Tempus Nativitatis", "Temps de Noël", LiturgicalColor.WHITE),
    TIME_AFTER_EPIPHANY("Tempus post Epiphaniam", "Temps après l'Épiphanie", LiturgicalColor.GREEN),
    SEPTUAGESIMA("Tempus Septuagesimae", "Temps de la Septuagésime", LiturgicalColor.VIOLET),
    LENT("Tempus Quadragesimae", "Temps du Carême", LiturgicalColor.VIOLET),
    PASSIONTIDE("Tempus Passionis", "Temps de la Passion", LiturgicalColor.VIOLET),
    EASTERTIDE("Tempus Paschale", "Temps Pascal", LiturgicalColor.WHITE),
    TIME_AFTER_PENTECOST("Tempus post Pentecosten", "Temps après la Pentecôte", LiturgicalColor.GREEN)
}

/**
 * Liturgical Classes (Rank) according to 1960 Rubrics (Rubricae Generales 1960).
 */
enum class LiturgicalClass(val romanNumeral: String, val frenchName: String, val precedence: Int) {
    FIRST_CLASS("I", "Ière classe", 1),
    SECOND_CLASS("II", "IIème classe", 2),
    THIRD_CLASS("III", "IIIème classe", 3),
    FOURTH_CLASS("IV", "IVème classe (Férie / Votive)", 4)
}

/**
 * Liturgical Colors with their visual representation and symbolic significance.
 */
enum class LiturgicalColor(
    val latinName: String,
    val frenchName: String,
    val hexColor: Long,
    val textColor: Long = 0xFFFFFFFF
) {
    WHITE("Albus", "Blanc", 0xFFF7F4EB, 0xFF7D1128),
    RED("Ruber", "Rouge", 0xFFB71C1C, 0xFFFFFFFF),
    GREEN("Viridis", "Vert", 0xFF1B5E20, 0xFFFFFFFF),
    VIOLET("Violaceus", "Violet", 0xFF4A148C, 0xFFFFFFFF),
    ROSE("Rosaceus", "Rose", 0xFFC2185B, 0xFFFFFFFF),
    BLACK("Niger", "Noir", 0xFF1A1A1A, 0xFFFFFFFF);

    val composeColor: Color get() = Color(hexColor)
    val composeTextColor: Color get() = Color(textColor)
}

/**
 * Seasonal Marian Antiphons for Compline (Completorium).
 */
enum class MarianAntiphon(
    val latinTitle: String,
    val frenchTitle: String,
    val periodDescription: String
) {
    ALMA_REDEMPTORIS_MATER(
        "Alma Redemptoris Mater",
        "Sainte Mère du Rédempteur",
        "Du 1er Dimanche de l'Avent jusqu'à la Purification (2 Février)"
    ),
    AVE_REGINA_CAELORUM(
        "Ave Regina Caelorum",
        "Salut, Reine des Cieux",
        "De la Purification jusqu'au Mercredi Saint"
    ),
    REGINA_CAELI(
        "Regina Caeli",
        "Reine du Ciel",
        "De Pâques jusqu'au Samedi après la Pentecôte"
    ),
    SALVE_REGINA(
        "Salve Regina",
        "Salut, ô Reine",
        "De la fête de la Très Sainte Trinité jusqu'à l'Avent"
    )
}

/**
 * Standard Kyriale settings assigned according to feast rank and season.
 */
enum class KyrialeSetting(
    val romanNumber: String,
    val title: String,
    val rubricUsage: String
) {
    KYRIALE_I("I", "Lux et origo", "Dans les solennités du Temps Pascal (Ière classe)"),
    KYRIALE_II("II", "Kyrie fons bonitatis", "Dans les solennités et fêtes de Ière classe"),
    KYRIALE_IV("IV", "Cunctipotens Genitor Deus", "Dans les fêtes des Apôtres et de IIème classe"),
    KYRIALE_VIII("VIII", "De Angelis", "Fêtes ordinaires de IIème et IIIème classe"),
    KYRIALE_IX("IX", "Cum jubilo", "Aux fêtes de la Sainte Vierge Marie"),
    KYRIALE_XI("XI", "Orbis factor", "Aux dimanches de l'année (après l'Épiphanie et Pentecôte)"),
    KYRIALE_XVII("XVII", "Pour les dimanches de l'Avent et du Carême", "Féries et dimanches pénitentiels"),
    KYRIALE_XVIII("XVIII", "Deus Genitor alme", "Féries de l'Avent et du Carême"),
    REQUIEM("Defunctorum", "Missa pro Defunctis", "Messes des Défunts / Requiem")
}

/**
 * Represents a single day in the Liturgical Year 1960.
 */
data class LiturgicalDay(
    val date: LocalDate,
    val title: String,
    val latinTitle: String,
    val season: LiturgicalSeason,
    val liturgicalClass: LiturgicalClass,
    val color: LiturgicalColor,
    val marianAntiphon: MarianAntiphon,
    val suggestedKyriale: KyrialeSetting,
    val commemorations: List<String> = emptyList(),
    val isSunday: Boolean = false,
    val isHolyDayOfObligation: Boolean = false,
    val notes: String = ""
)

/**
 * Speakers/Voices in Liturgical dialogue.
 */
enum class Speaker(val symbol: String, val label: String) {
    PRIEST("℣.", "Sacerdos"),
    SERVER("℟.", "Minister / Chorus"),
    CELEBRANT("C.", "Celebrans"),
    LECTOR("L.", "Lector"),
    ALL("Omnes", "Tous")
}

/**
 * Represents a liturgical item for rich rendering in Compose.
 */
sealed class LiturgicalItem {
    data class Rubric(val text: String) : LiturgicalItem()
    data class HeaderSection(val latin: String, val vernacular: String? = null) : LiturgicalItem()
    data class Title(val latin: String, val vernacular: String? = null, val subtitle: String? = null) : LiturgicalItem()
    data class BilingualText(
        val latin: String,
        val vernacular: String,
        val speaker: Speaker? = null,
        val dropCap: Boolean = false
    ) : LiturgicalItem()
    data class VersicleResponse(
        val versicleLatin: String,
        val versicleVernacular: String,
        val responseLatin: String,
        val responseVernacular: String
    ) : LiturgicalItem()
    data class ChantScore(
        val title: String,
        val mode: String?,
        val gabc: String,
        val translation: String? = null
    ) : LiturgicalItem()
    data class InstructionNote(val note: String) : LiturgicalItem()
}
