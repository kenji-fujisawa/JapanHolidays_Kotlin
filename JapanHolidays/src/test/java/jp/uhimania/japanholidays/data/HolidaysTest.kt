package jp.uhimania.japanholidays.data

import jp.uhimania.japanholidays.Holidays
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class HolidaysTest {
    @Test
    fun testIsHoliday() {
        assertTrue(Holidays.isHoliday(2026, 1, 12))
        assertFalse(Holidays.isHoliday(2025, 1, 12))

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        assertTrue(Holidays.isHoliday(formatter.parse("2026-01-12") ?: Date()))
        assertFalse(Holidays.isHoliday(formatter.parse("2025-01-12") ?: Date()))

        assertTrue(Holidays.isHoliday(LocalDate.of(2026, 1, 12)))
        assertFalse(Holidays.isHoliday(LocalDate.of(2025, 1, 12)))
    }

    @Test
    fun testGetName() {
        assertEquals("成人の日", Holidays.getName(2026, 1, 12))
        assertNull(Holidays.getName(2025, 1, 12))

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        assertEquals("成人の日", Holidays.getName(formatter.parse("2026-01-12") ?: Date()))
        assertNull(Holidays.getName(formatter.parse("2025-01-12") ?: Date()))

        assertEquals("成人の日", Holidays.getName(LocalDate.of(2026, 1, 12)))
        assertNull(Holidays.getName(LocalDate.of(2025, 1, 12)))
    }
}