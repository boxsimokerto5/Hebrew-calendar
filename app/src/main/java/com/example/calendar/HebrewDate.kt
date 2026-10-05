package com.example.calendar

import com.example.localization.AppLanguage
import java.time.LocalDate

data class HebrewDate(
    val year: Int,
    val monthCode: Int, // Maps to android.icu.util.HebrewCalendar constants
    val monthNameEn: String,
    val monthNameId: String,
    val monthNameHe: String,
    val day: Int,
    val gregorianDate: LocalDate,
    val isLeapYear: Boolean,
    val dayOfWeek: Int // 1 = Sunday, 7 = Saturday
) {
    fun getFormattedHebrew(language: AppLanguage): String {
        val monthName = when (language) {
            AppLanguage.HEBREW -> monthNameHe
            AppLanguage.INDONESIAN -> monthNameId
            AppLanguage.ENGLISH -> monthNameEn
        }
        val dayString = if (language == AppLanguage.HEBREW) {
            HebrewCalendarEngine.toGematria(day)
        } else {
            day.toString()
        }
        val yearString = if (language == AppLanguage.HEBREW) {
            HebrewCalendarEngine.toHebrewYearString(year)
        } else {
            year.toString()
        }

        return if (language == AppLanguage.HEBREW) {
            "$dayString $monthName $yearString"
        } else {
            "$dayString $monthName $yearString"
        }
    }

    val hebrewDayLetter: String
        get() = HebrewCalendarEngine.toGematria(day)

    val hebrewYearGematria: String
        get() = HebrewCalendarEngine.toHebrewYearString(year)

    fun getDayOfWeekName(language: AppLanguage): String {
        return when (dayOfWeek) {
            1 -> when (language) {
                AppLanguage.HEBREW -> "יום ראשון"
                AppLanguage.INDONESIAN -> "Minggu (Yom Rishon)"
                AppLanguage.ENGLISH -> "Sunday (Yom Rishon)"
            }
            2 -> when (language) {
                AppLanguage.HEBREW -> "יום שני"
                AppLanguage.INDONESIAN -> "Senin (Yom Sheni)"
                AppLanguage.ENGLISH -> "Monday (Yom Sheni)"
            }
            3 -> when (language) {
                AppLanguage.HEBREW -> "יום שלישי"
                AppLanguage.INDONESIAN -> "Selasa (Yom Shlishi)"
                AppLanguage.ENGLISH -> "Tuesday (Yom Shlishi)"
            }
            4 -> when (language) {
                AppLanguage.HEBREW -> "יום רביעי"
                AppLanguage.INDONESIAN -> "Rabu (Yom Revi'i)"
                AppLanguage.ENGLISH -> "Wednesday (Yom Revi'i)"
            }
            5 -> when (language) {
                AppLanguage.HEBREW -> "יום חמישי"
                AppLanguage.INDONESIAN -> "Kamis (Yom Chamishi)"
                AppLanguage.ENGLISH -> "Thursday (Yom Chamishi)"
            }
            6 -> when (language) {
                AppLanguage.HEBREW -> "יום שישי"
                AppLanguage.INDONESIAN -> "Jumat (Erev Shabbat)"
                AppLanguage.ENGLISH -> "Friday (Erev Shabbat)"
            }
            7 -> when (language) {
                AppLanguage.HEBREW -> "שַׁבָּת"
                AppLanguage.INDONESIAN -> "Sabtu (Shabbat Kodesh)"
                AppLanguage.ENGLISH -> "Saturday (Shabbat)"
            }
            else -> ""
        }
    }
}
