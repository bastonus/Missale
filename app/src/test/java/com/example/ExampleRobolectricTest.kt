package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.liturgy.calendar.LiturgicalCalendar
import com.example.liturgy.gabc.GabcParser
import com.example.liturgy.model.LiturgicalSeason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.time.Month

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Missale & Breviarium", appName)
    }

    @Test
    fun `test easter computus calculation`() {
        // In 2026, Easter Sunday is on April 5
        val easter2026 = LiturgicalCalendar.getEasterSunday(2026)
        assertEquals(LocalDate.of(2026, Month.APRIL, 5), easter2026)

        // In 2024, Easter was on March 31
        val easter2024 = LiturgicalCalendar.getEasterSunday(2024)
        assertEquals(LocalDate.of(2024, Month.MARCH, 31), easter2024)
    }

    @Test
    fun `test gabc parser extracts clef and words`() {
        val gabc = "(c4) Puer(d) na(d)tus(c) est(d) no(ef)bis.(ed..)"
        val score = GabcParser.parse(gabc)
        assertNotNull(score)
        assertEquals(4, score.clef.line)
        assertTrue(score.words.isNotEmpty())
    }

    @Test
    fun `test liturgical day computation for Christmas`() {
        val christmasDay = LiturgicalCalendar.computeDay(LocalDate.of(2026, Month.DECEMBER, 25))
        assertEquals(LiturgicalSeason.CHRISTMASTIDE, christmasDay.season)
        assertTrue(christmasDay.title.contains("Nativité") || christmasDay.title.contains("Noël"))
    }
}
