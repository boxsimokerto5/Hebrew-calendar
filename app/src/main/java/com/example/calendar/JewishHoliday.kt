package com.example.calendar

import com.example.localization.AppLanguage
import java.time.LocalDate

enum class HolidayCategory {
    MAJOR_YOM_TOV,
    MINOR_HOLIDAY,
    FAST_DAY,
    ROSH_CHODESH,
    MODERN_MEMORIAL,
    SHABBAT
}

data class JewishHoliday(
    val id: String,
    val nameHe: String,
    val nameId: String,
    val nameEn: String,
    val category: HolidayCategory,
    val monthCode: Int, // android.icu.util.HebrewCalendar month code
    val dayStart: Int,
    val dayEnd: Int = dayStart,
    val descriptionHe: String,
    val descriptionId: String,
    val descriptionEn: String,
    val greetingHe: String = "",
    val greetingId: String = "",
    val greetingEn: String = "",
    val traditionsHe: String = "",
    val traditionsId: String = "",
    val traditionsEn: String = "",
    val isWorkProhibited: Boolean = false
) {
    fun getName(language: AppLanguage): String {
        return when (language) {
            AppLanguage.HEBREW -> nameHe
            AppLanguage.INDONESIAN -> nameId
            AppLanguage.ENGLISH -> nameEn
        }
    }

    fun getDescription(language: AppLanguage): String {
        return when (language) {
            AppLanguage.HEBREW -> descriptionHe
            AppLanguage.INDONESIAN -> descriptionId
            AppLanguage.ENGLISH -> descriptionEn
        }
    }

    fun getGreeting(language: AppLanguage): String {
        return when (language) {
            AppLanguage.HEBREW -> greetingHe
            AppLanguage.INDONESIAN -> greetingId
            AppLanguage.ENGLISH -> greetingEn
        }
    }

    fun getTraditions(language: AppLanguage): String {
        return when (language) {
            AppLanguage.HEBREW -> traditionsHe
            AppLanguage.INDONESIAN -> traditionsId
            AppLanguage.ENGLISH -> traditionsEn
        }
    }

    fun getCategoryName(language: AppLanguage): String {
        return when (category) {
            HolidayCategory.MAJOR_YOM_TOV -> when (language) {
                AppLanguage.HEBREW -> "חג מן התורה (יום טוב)"
                AppLanguage.INDONESIAN -> "Hari Raya Utama (Yom Tov)"
                AppLanguage.ENGLISH -> "Major Holiday (Yom Tov)"
            }
            HolidayCategory.MINOR_HOLIDAY -> when (language) {
                AppLanguage.HEBREW -> "חג מדרבנן / מועד"
                AppLanguage.INDONESIAN -> "Hari Raya Tambahan"
                AppLanguage.ENGLISH -> "Minor Holiday"
            }
            HolidayCategory.FAST_DAY -> when (language) {
                AppLanguage.HEBREW -> "יום צום ותענית"
                AppLanguage.INDONESIAN -> "Hari Puasa (Ta'anit)"
                AppLanguage.ENGLISH -> "Fast Day"
            }
            HolidayCategory.ROSH_CHODESH -> when (language) {
                AppLanguage.HEBREW -> "ראש חודש"
                AppLanguage.INDONESIAN -> "Bulan Baru (Rosh Chodesh)"
                AppLanguage.ENGLISH -> "New Month (Rosh Chodesh)"
            }
            HolidayCategory.MODERN_MEMORIAL -> when (language) {
                AppLanguage.HEBREW -> "יום זיכרון לאומי"
                AppLanguage.INDONESIAN -> "Peringatan Nasional"
                AppLanguage.ENGLISH -> "National Commemoration"
            }
            HolidayCategory.SHABBAT -> when (language) {
                AppLanguage.HEBREW -> "שבת קודש"
                AppLanguage.INDONESIAN -> "Hari Shabat"
                AppLanguage.ENGLISH -> "Shabbat"
            }
        }
    }
}

data class HolidayInstance(
    val holiday: JewishHoliday,
    val hebrewDate: HebrewDate,
    val gregorianDate: LocalDate,
    val dayNumberInHoliday: Int = 1,
    val totalDays: Int = 1
)
