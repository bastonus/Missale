package com.example.liturgy.calendar

import com.example.liturgy.model.KyrialeSetting
import com.example.liturgy.model.LiturgicalClass
import com.example.liturgy.model.LiturgicalColor
import com.example.liturgy.model.LiturgicalDay
import com.example.liturgy.model.LiturgicalSeason
import com.example.liturgy.model.MarianAntiphon
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.ChronoUnit

/**
 * Perpetual Liturgical Calendar Engine according to the 1960 Code of Rubrics
 * (Missale Romanum & Breviarium Romanum 1960).
 */
object LiturgicalCalendar {

    /**
     * Calculates the date of Easter Sunday using the Meeus/Jones/Butcher Gregorian algorithm.
     */
    fun getEasterSunday(year: Int): LocalDate {
        val a = year % 19
        val b = year / 100
        val c = year % 100
        val d = b / 4
        val e = b % 4
        val f = (b + 8) / 25
        val g = (b - f + 1) / 3
        val h = (19 * a + b - d - g + 15) % 30
        val i = c / 4
        val k = c % 4
        val l = (32 + 2 * e + 2 * i - h - k) % 7
        val m = (a + 11 * h + 22 * l) / 451
        val month = (h + l - 7 * m + 114) / 31
        val day = ((h + l - 7 * m + 114) % 31) + 1
        return LocalDate.of(year, month, day)
    }

    /**
     * Calculates the 1st Sunday of Advent for the given year (Sunday between Nov 27 and Dec 3).
     */
    fun getFirstSundayOfAdvent(year: Int): LocalDate {
        val stAndrew = LocalDate.of(year, Month.NOVEMBER, 30)
        val dayOfWeek = stAndrew.dayOfWeek
        val daysUntilSunday = (7 - dayOfWeek.value % 7) % 7
        val candidate = stAndrew.plusDays(daysUntilSunday.toLong())
        return if (candidate.isAfter(LocalDate.of(year, Month.DECEMBER, 3))) {
            candidate.minusDays(7)
        } else {
            candidate
        }
    }

    /**
     * Calculates the Last Sunday of October (Christ the King in the 1960 calendar).
     */
    fun getChristTheKingSunday(year: Int): LocalDate {
        var date = LocalDate.of(year, Month.OCTOBER, 31)
        while (date.dayOfWeek != DayOfWeek.SUNDAY) {
            date = date.minusDays(1)
        }
        return date
    }

    /**
     * Computes the complete LiturgicalDay for any given date.
     */
    fun computeDay(date: LocalDate): LiturgicalDay {
        val year = date.year
        val easter = getEasterSunday(year)
        val advent1 = getFirstSundayOfAdvent(year)
        val prevAdvent1 = getFirstSundayOfAdvent(year - 1)

        val daysFromEaster = ChronoUnit.DAYS.between(easter, date)

        // 1. Check Temporale (Movable Cycle)
        val temporale = computeTemporaleDay(date, year, easter, advent1, prevAdvent1, daysFromEaster)

        // 2. Check Sanctorale (Fixed Feasts)
        val sanctorale = computeSanctoraleDay(date)

        // 3. Resolve Precedence (1960 Rubrics)
        val resolved = if (sanctorale != null && temporale != null) {
            if (sanctorale.liturgicalClass.precedence < temporale.liturgicalClass.precedence) {
                sanctorale.copy(
                    commemorations = listOf("De tempore: " + temporale.title) + sanctorale.commemorations,
                    season = temporale.season,
                    marianAntiphon = determineMarianAntiphon(date, easter, advent1, prevAdvent1)
                )
            } else if (sanctorale.liturgicalClass.precedence == temporale.liturgicalClass.precedence && !temporale.isSunday) {
                sanctorale.copy(
                    commemorations = listOf("De tempore: " + temporale.title) + sanctorale.commemorations,
                    season = temporale.season,
                    marianAntiphon = determineMarianAntiphon(date, easter, advent1, prevAdvent1)
                )
            } else {
                temporale.copy(
                    commemorations = listOf("Commemoratio: " + sanctorale.title) + temporale.commemorations,
                    marianAntiphon = determineMarianAntiphon(date, easter, advent1, prevAdvent1)
                )
            }
        } else {
            sanctorale ?: temporale ?: createDefaultFerialDay(date, easter, advent1, prevAdvent1)
        }

        // Assign Kyriale according to 1960 rubrics
        val kyriale = determineKyriale(resolved)

        return resolved.copy(
            suggestedKyriale = kyriale,
            marianAntiphon = determineMarianAntiphon(date, easter, advent1, prevAdvent1)
        )
    }

    private fun determineMarianAntiphon(
        date: LocalDate,
        easter: LocalDate,
        advent1: LocalDate,
        prevAdvent1: LocalDate
    ): MarianAntiphon {
        val year = date.year
        val candlemas = LocalDate.of(year, Month.FEBRUARY, 2)
        val holyWednesday = easter.minusDays(4)
        val pentecostSaturday = easter.plusDays(55)

        return when {
            // From 1st Sunday of Advent until Feb 2 (inclusive)
            date >= advent1 || date <= candlemas -> MarianAntiphon.ALMA_REDEMPTORIS_MATER
            // From Feb 3 until Holy Wednesday
            date > candlemas && date <= holyWednesday -> MarianAntiphon.AVE_REGINA_CAELORUM
            // From Holy Saturday/Easter until Saturday after Pentecost
            date >= easter.minusDays(1) && date <= pentecostSaturday -> MarianAntiphon.REGINA_CAELI
            // From Trinity Sunday until Advent
            else -> MarianAntiphon.SALVE_REGINA
        }
    }

    private fun determineKyriale(day: LiturgicalDay): KyrialeSetting {
        if (day.title.contains("Defunctorum", ignoreCase = true) || day.color == LiturgicalColor.BLACK) {
            return KyrialeSetting.REQUIEM
        }
        if (day.title.contains("Beatae Mariae Virginis", ignoreCase = true) ||
            day.title.contains("Sainte Vierge", ignoreCase = true) ||
            day.title.contains("Maria", ignoreCase = true)
        ) {
            return KyrialeSetting.KYRIALE_IX
        }
        if (day.season == LiturgicalSeason.EASTERTIDE && day.liturgicalClass == LiturgicalClass.FIRST_CLASS) {
            return KyrialeSetting.KYRIALE_I
        }
        if (day.isSunday && day.season in listOf(LiturgicalSeason.TIME_AFTER_EPIPHANY, LiturgicalSeason.TIME_AFTER_PENTECOST)) {
            return KyrialeSetting.KYRIALE_XI
        }
        if (day.season in listOf(LiturgicalSeason.ADVENT, LiturgicalSeason.LENT, LiturgicalSeason.PASSIONTIDE)) {
            return if (day.isSunday) KyrialeSetting.KYRIALE_XVII else KyrialeSetting.KYRIALE_XVIII
        }
        return when (day.liturgicalClass) {
            LiturgicalClass.FIRST_CLASS -> KyrialeSetting.KYRIALE_II
            LiturgicalClass.SECOND_CLASS -> KyrialeSetting.KYRIALE_IV
            LiturgicalClass.THIRD_CLASS -> KyrialeSetting.KYRIALE_VIII
            LiturgicalClass.FOURTH_CLASS -> KyrialeSetting.KYRIALE_XVIII
        }
    }

    private fun computeTemporaleDay(
        date: LocalDate,
        year: Int,
        easter: LocalDate,
        advent1: LocalDate,
        prevAdvent1: LocalDate,
        daysFromEaster: Long
    ): LiturgicalDay? {
        val isSunday = date.dayOfWeek == DayOfWeek.SUNDAY

        // Movable cycle relative to Easter
        when (daysFromEaster) {
            -63L -> return LiturgicalDay(
                date, "Dimanche de la Septuagésime", "Dominica in Septuagesima",
                LiturgicalSeason.SEPTUAGESIMA, LiturgicalClass.SECOND_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -56L -> return LiturgicalDay(
                date, "Dimanche de la Sexagésime", "Dominica in Sexagesima",
                LiturgicalSeason.SEPTUAGESIMA, LiturgicalClass.SECOND_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -49L -> return LiturgicalDay(
                date, "Dimanche de la Quinquagésime", "Dominica in Quinquagesima",
                LiturgicalSeason.SEPTUAGESIMA, LiturgicalClass.SECOND_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -46L -> return LiturgicalDay(
                date, "Mercredi des Cendres", "Feria IV Cinerum",
                LiturgicalSeason.LENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = false
            )
            -42L -> return LiturgicalDay(
                date, "Ier Dimanche de Carême", "Dominica I in Quadragesima",
                LiturgicalSeason.LENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -35L -> return LiturgicalDay(
                date, "IIe Dimanche de Carême", "Dominica II in Quadragesima",
                LiturgicalSeason.LENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -28L -> return LiturgicalDay(
                date, "IIIe Dimanche de Carême", "Dominica III in Quadragesima",
                LiturgicalSeason.LENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -21L -> return LiturgicalDay(
                date, "IVe Dimanche de Carême (Laetare)", "Dominica IV in Quadragesima (Laetare)",
                LiturgicalSeason.LENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.ROSE,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -14L -> return LiturgicalDay(
                date, "Ier Dimanche de la Passion", "Dominica I Passionis",
                LiturgicalSeason.PASSIONTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -7L -> return LiturgicalDay(
                date, "Dimanche des Rameaux et de la Passion", "Dominica II Passionis seu in Palmis",
                LiturgicalSeason.PASSIONTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.RED,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
            -3L -> return LiturgicalDay(
                date, "Jeudi Saint in Coena Domini", "Feria V in Cena Domini",
                LiturgicalSeason.PASSIONTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_IV, isSunday = false
            )
            -2L -> return LiturgicalDay(
                date, "Vendredi Saint de la Passion et Mort du Seigneur", "Feria VI in Passione et Morte Domini",
                LiturgicalSeason.PASSIONTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.BLACK,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.REQUIEM, isSunday = false
            )
            -1L -> return LiturgicalDay(
                date, "Samedi Saint (Vigile Pascale)", "Sabbato Sancto in Vigilia Paschali",
                LiturgicalSeason.PASSIONTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.REGINA_CAELI, KyrialeSetting.KYRIALE_I, isSunday = false
            )
            0L -> return LiturgicalDay(
                date, "Dimanche de la Résurrection du Seigneur (Pâques)", "Dominica Resurrectionis Domini Nostri Jesu Christi",
                LiturgicalSeason.EASTERTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.REGINA_CAELI, KyrialeSetting.KYRIALE_I, isSunday = true, isHolyDayOfObligation = true
            )
            7L -> return LiturgicalDay(
                date, "Dimanche in Albis (Quasimodo)", "Dominica in Albis in Octava Paschae",
                LiturgicalSeason.EASTERTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.REGINA_CAELI, KyrialeSetting.KYRIALE_I, isSunday = true
            )
            39L -> return LiturgicalDay(
                date, "Ascension de Notre Seigneur", "In Ascensione Domini",
                LiturgicalSeason.EASTERTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.REGINA_CAELI, KyrialeSetting.KYRIALE_I, isSunday = false, isHolyDayOfObligation = true
            )
            49L -> return LiturgicalDay(
                date, "Dimanche de la Pentecôte", "Dominica Pentecostes",
                LiturgicalSeason.EASTERTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.RED,
                MarianAntiphon.REGINA_CAELI, KyrialeSetting.KYRIALE_I, isSunday = true, isHolyDayOfObligation = true
            )
            56L -> return LiturgicalDay(
                date, "Fête de la Très Sainte Trinité", "In Festo Sanctissimae Trinitatis",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_II, isSunday = true
            )
            60L -> return LiturgicalDay(
                date, "Fête du Très Saint Sacrement (Corpus Christi)", "Sanctissimi Corporis Christi",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_II, isSunday = false, isHolyDayOfObligation = true
            )
            68L -> return LiturgicalDay(
                date, "Sacré-Cœur de Jésus", "Sanctissimi Cordis Jesu",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_II, isSunday = false
            )
        }

        // Check Christ the King
        val christTheKing = getChristTheKingSunday(year)
        if (date == christTheKing) {
            return LiturgicalDay(
                date, "Fête de Notre-Seigneur Jésus-Christ Roi", "Domini Nostri Jesu Christi Regis",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_II, isSunday = true
            )
        }

        // Advent Sundays
        if (date == advent1) {
            return LiturgicalDay(
                date, "Ier Dimanche de l'Avent", "Dominica I Adventus",
                LiturgicalSeason.ADVENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
        }
        if (date == advent1.plusWeeks(1)) {
            return LiturgicalDay(
                date, "IIe Dimanche de l'Avent", "Dominica II Adventus",
                LiturgicalSeason.ADVENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
        }
        if (date == advent1.plusWeeks(2)) {
            return LiturgicalDay(
                date, "IIIe Dimanche de l'Avent (Gaudete)", "Dominica III Adventus (Gaudete)",
                LiturgicalSeason.ADVENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.ROSE,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
        }
        if (date == advent1.plusWeeks(3)) {
            return LiturgicalDay(
                date, "IVe Dimanche de l'Avent", "Dominica IV Adventus",
                LiturgicalSeason.ADVENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_XVII, isSunday = true
            )
        }

        // Post-Pentecost Sundays calculation
        val trinitySunday = easter.plusDays(56)
        if (isSunday && date.isAfter(trinitySunday) && date.isBefore(advent1)) {
            val weekNumber = ChronoUnit.WEEKS.between(trinitySunday, date).toInt() + 1
            return LiturgicalDay(
                date, "${weekNumber}e Dimanche après la Pentecôte", "Dominica $weekNumber post Pentecosten",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.SECOND_CLASS, LiturgicalColor.GREEN,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_XI, isSunday = true
            )
        }

        // Eastertide Sundays
        if (isSunday && daysFromEaster in 8..48) {
            val weekNumber = (daysFromEaster / 7).toInt() + 1
            return LiturgicalDay(
                date, "${weekNumber}e Dimanche après Pâques", "Dominica $weekNumber post Pascha",
                LiturgicalSeason.EASTERTIDE, LiturgicalClass.SECOND_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.REGINA_CAELI, KyrialeSetting.KYRIALE_I, isSunday = true
            )
        }

        return null
    }

    private fun computeSanctoraleDay(date: LocalDate): LiturgicalDay? {
        val month = date.month
        val day = date.dayOfMonth

        return when {
            month == Month.JANUARY && day == 1 -> LiturgicalDay(
                date, "Octave de la Nativité / Circoncision", "In Octava Nativitatis Domini",
                LiturgicalSeason.CHRISTMASTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_II, isHolyDayOfObligation = true
            )
            month == Month.JANUARY && day == 6 -> LiturgicalDay(
                date, "Épiphanie de Notre Seigneur", "In Epiphania Domini",
                LiturgicalSeason.CHRISTMASTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_II, isHolyDayOfObligation = true
            )
            month == Month.FEBRUARY && day == 2 -> LiturgicalDay(
                date, "Purification de la B.V.M. (Chandeleur)", "In Purificatione B. Mariae Virginis",
                LiturgicalSeason.TIME_AFTER_EPIPHANY, LiturgicalClass.SECOND_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_IX
            )
            month == Month.MARCH && day == 19 -> LiturgicalDay(
                date, "Saint Joseph, Époux de la B.V.M.", "S. Joseph Sponsi B. Mariae Virginis",
                LiturgicalSeason.LENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_II
            )
            month == Month.MARCH && day == 25 -> LiturgicalDay(
                date, "Annonciation de la B.V.M.", "In Annuntiatione B. Mariae Virginis",
                LiturgicalSeason.LENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.AVE_REGINA_CAELORUM, KyrialeSetting.KYRIALE_IX
            )
            month == Month.MAY && day == 1 -> LiturgicalDay(
                date, "Saint Joseph Artisan", "S. Joseph Opificis",
                LiturgicalSeason.EASTERTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.REGINA_CAELI, KyrialeSetting.KYRIALE_II
            )
            month == Month.JUNE && day == 24 -> LiturgicalDay(
                date, "Nativité de Saint Jean-Baptiste", "In Nativitate S. Joannis Baptistae",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_II
            )
            month == Month.JUNE && day == 29 -> LiturgicalDay(
                date, "Saints Pierre et Paul, Apôtres", "Ss. Petri et Pauli Apostolorum",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.RED,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_IV, isHolyDayOfObligation = true
            )
            month == Month.JULY && day == 1 -> LiturgicalDay(
                date, "Très Précieux Sang de N.-S. J.-C.", "Pretiosissimi Sanguinis D.N.J.C.",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.RED,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_II
            )
            month == Month.AUGUST && day == 15 -> LiturgicalDay(
                date, "Assomption de la B.V.M.", "In Assumptione B. Mariae Virginis",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_IX, isHolyDayOfObligation = true
            )
            month == Month.SEPTEMBER && day == 8 -> LiturgicalDay(
                date, "Nativité de la B.V.M.", "In Nativitate B. Mariae Virginis",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.SECOND_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_IX
            )
            month == Month.SEPTEMBER && day == 14 -> LiturgicalDay(
                date, "Exaltation de la Sainte Croix", "In Exaltatione Sanctae Crucis",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.SECOND_CLASS, LiturgicalColor.RED,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_IV
            )
            month == Month.SEPTEMBER && day == 29 -> LiturgicalDay(
                date, "Dédicace de Saint Michel Archange", "In Dedicatione S. Michaelis Archangeli",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_II
            )
            month == Month.NOVEMBER && day == 1 -> LiturgicalDay(
                date, "Toussaint", "Omnium Sanctorum",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.KYRIALE_II, isHolyDayOfObligation = true
            )
            month == Month.NOVEMBER && day == 2 -> LiturgicalDay(
                date, "Commémoration de tous les fidèles défunts", "In Commemoratione Omnium Fidelium Defunctorum",
                LiturgicalSeason.TIME_AFTER_PENTECOST, LiturgicalClass.FIRST_CLASS, LiturgicalColor.BLACK,
                MarianAntiphon.SALVE_REGINA, KyrialeSetting.REQUIEM
            )
            month == Month.DECEMBER && day == 8 -> LiturgicalDay(
                date, "Immaculée Conception de la B.V.M.", "In Conceptione Immaculata B. Mariae Virginis",
                LiturgicalSeason.ADVENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_IX, isHolyDayOfObligation = true
            )
            month == Month.DECEMBER && day == 24 -> LiturgicalDay(
                date, "Vigile de la Nativité", "In Vigilia Nativitatis Domini",
                LiturgicalSeason.ADVENT, LiturgicalClass.FIRST_CLASS, LiturgicalColor.VIOLET,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_XVII
            )
            month == Month.DECEMBER && day == 25 -> LiturgicalDay(
                date, "Nativité de Notre-Seigneur Jésus-Christ (Noël)", "In Nativitate Domini",
                LiturgicalSeason.CHRISTMASTIDE, LiturgicalClass.FIRST_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_II, isHolyDayOfObligation = true
            )
            month == Month.DECEMBER && day == 26 -> LiturgicalDay(
                date, "Saint Étienne, Premier Martyr", "S. Stephani Protomartyris",
                LiturgicalSeason.CHRISTMASTIDE, LiturgicalClass.SECOND_CLASS, LiturgicalColor.RED,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_IV
            )
            month == Month.DECEMBER && day == 27 -> LiturgicalDay(
                date, "Saint Jean, Apôtre et Évangéliste", "S. Joannis Apostoli et Evangelistae",
                LiturgicalSeason.CHRISTMASTIDE, LiturgicalClass.SECOND_CLASS, LiturgicalColor.WHITE,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_IV
            )
            month == Month.DECEMBER && day == 28 -> LiturgicalDay(
                date, "Saints Innocents Martyrs", "Ss. Innocentium Martyrum",
                LiturgicalSeason.CHRISTMASTIDE, LiturgicalClass.SECOND_CLASS, LiturgicalColor.RED,
                MarianAntiphon.ALMA_REDEMPTORIS_MATER, KyrialeSetting.KYRIALE_IV
            )
            else -> null
        }
    }

    private fun createDefaultFerialDay(
        date: LocalDate,
        easter: LocalDate,
        advent1: LocalDate,
        prevAdvent1: LocalDate
    ): LiturgicalDay {
        val daysFromEaster = ChronoUnit.DAYS.between(easter, date)
        val season = when {
            date >= advent1 || (date.month == Month.DECEMBER && date.dayOfMonth < 25) -> LiturgicalSeason.ADVENT
            (date.month == Month.DECEMBER && date.dayOfMonth >= 25) || (date.month == Month.JANUARY && date.dayOfMonth <= 13) -> LiturgicalSeason.CHRISTMASTIDE
            daysFromEaster < -63 -> LiturgicalSeason.TIME_AFTER_EPIPHANY
            daysFromEaster in -63..-47 -> LiturgicalSeason.SEPTUAGESIMA
            daysFromEaster in -46..-15 -> LiturgicalSeason.LENT
            daysFromEaster in -14..-1 -> LiturgicalSeason.PASSIONTIDE
            daysFromEaster in 0..55 -> LiturgicalSeason.EASTERTIDE
            else -> LiturgicalSeason.TIME_AFTER_PENTECOST
        }

        val color = season.defaultColor
        val dayName = when (date.dayOfWeek) {
            DayOfWeek.MONDAY -> "Feria II (Lundi)"
            DayOfWeek.TUESDAY -> "Feria III (Mardi)"
            DayOfWeek.WEDNESDAY -> "Feria IV (Mercredi)"
            DayOfWeek.THURSDAY -> "Feria V (Jeudi)"
            DayOfWeek.FRIDAY -> "Feria VI (Vendredi)"
            DayOfWeek.SATURDAY -> "Sabbato (Samedi)"
            DayOfWeek.SUNDAY -> "Dominica (Dimanche)"
        }

        return LiturgicalDay(
            date = date,
            title = "$dayName - $season",
            latinTitle = "$dayName infra annum",
            season = season,
            liturgicalClass = if (date.dayOfWeek == DayOfWeek.SUNDAY) LiturgicalClass.SECOND_CLASS else LiturgicalClass.FOURTH_CLASS,
            color = color,
            marianAntiphon = determineMarianAntiphon(date, easter, advent1, prevAdvent1),
            suggestedKyriale = if (season == LiturgicalSeason.ADVENT || season == LiturgicalSeason.LENT) KyrialeSetting.KYRIALE_XVIII else KyrialeSetting.KYRIALE_XI,
            isSunday = date.dayOfWeek == DayOfWeek.SUNDAY
        )
    }
}
