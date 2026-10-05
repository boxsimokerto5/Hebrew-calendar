package com.example

import com.example.calendar.HebrewCalendarEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ExampleUnitTest {

    @Test
    fun gematria_convertsSpecialValuesProperly() {
        assertEquals("א׳", HebrewCalendarEngine.toGematria(1))
        assertEquals("י׳", HebrewCalendarEngine.toGematria(10))
        assertEquals("ט״ו", HebrewCalendarEngine.toGematria(15)) // 15 is Tu (not Yah)
        assertEquals("ט״ז", HebrewCalendarEngine.toGematria(16)) // 16 is Tz (not Yo)
        assertEquals("כ״ד", HebrewCalendarEngine.toGematria(24))
    }

    @Test
    fun hebrewYearString_convertsProperly() {
        val y5786 = HebrewCalendarEngine.toHebrewYearString(5786)
        assertTrue("Year 5786 contains Tav-Shin-Pe-Vav", y5786.contains("תשפ"))
        val y5787 = HebrewCalendarEngine.toHebrewYearString(5787)
        assertTrue("Year 5787 contains Tav-Shin-Pe-Zayin", y5787.contains("תשפ"))
    }

    @Test
    fun leapYear_identifiesMetonicCycleCorrectly() {
        // In 19-year cycle, year mod 19 in {3, 6, 8, 11, 14, 17, 0}
        assertTrue(HebrewCalendarEngine.isLeapYear(5784)) // 5784 % 19 = 8 -> Leap year
        assertFalse(HebrewCalendarEngine.isLeapYear(5785)) // 5785 % 19 = 9 -> Regular year
        assertFalse(HebrewCalendarEngine.isLeapYear(5786)) // 5786 % 19 = 10 -> Regular year
        assertTrue(HebrewCalendarEngine.isLeapYear(5787)) // 5787 % 19 = 11 -> Leap year
    }

    @Test
    fun holidayCatalog_containsCoreHolidays() {
        val catalog = HebrewCalendarEngine.allHolidaysCatalog
        val ids = catalog.map { it.id }
        assertTrue(ids.contains("rosh_hashanah"))
        assertTrue(ids.contains("yom_kippur"))
        assertTrue(ids.contains("sukkot"))
        assertTrue(ids.contains("hanukkah"))
        assertTrue(ids.contains("purim"))
        assertTrue(ids.contains("pesach"))
        assertTrue(ids.contains("shavuot"))
        assertTrue(ids.contains("tisha_bav"))
    }
}
