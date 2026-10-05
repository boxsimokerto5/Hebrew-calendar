package com.example.localization

object StringResources {

    fun get(key: String, language: AppLanguage): String {
        return strings[key]?.get(language) ?: strings[key]?.get(AppLanguage.ENGLISH) ?: key
    }

    private val strings: Map<String, Map<AppLanguage, String>> = mapOf(
        "app_title" to mapOf(
            AppLanguage.INDONESIAN to "Kalender Ibrani (Luach)",
            AppLanguage.HEBREW to "לוח שנה עברי",
            AppLanguage.ENGLISH to "Hebrew Calendar (Luach)"
        ),
        "tab_calendar" to mapOf(
            AppLanguage.INDONESIAN to "Kalender",
            AppLanguage.HEBREW to "לוח שנה",
            AppLanguage.ENGLISH to "Calendar"
        ),
        "tab_holidays" to mapOf(
            AppLanguage.INDONESIAN to "Hari Raya",
            AppLanguage.HEBREW to "חגים ומועדים",
            AppLanguage.ENGLISH to "Holidays"
        ),
        "tab_planner" to mapOf(
            AppLanguage.INDONESIAN to "Rencana",
            AppLanguage.HEBREW to "תוכניות",
            AppLanguage.ENGLISH to "Planner"
        ),
        "tab_settings" to mapOf(
            AppLanguage.INDONESIAN to "Pengaturan",
            AppLanguage.HEBREW to "הגדרות",
            AppLanguage.ENGLISH to "Settings"
        ),
        "today" to mapOf(
            AppLanguage.INDONESIAN to "Hari Ini",
            AppLanguage.HEBREW to "היום",
            AppLanguage.ENGLISH to "Today"
        ),
        "selected_date" to mapOf(
            AppLanguage.INDONESIAN to "Tanggal Terpilih",
            AppLanguage.HEBREW to "תאריך נבחר",
            AppLanguage.ENGLISH to "Selected Date"
        ),
        "hebrew_date" to mapOf(
            AppLanguage.INDONESIAN to "Tanggal Ibrani",
            AppLanguage.HEBREW to "תאריך עברי",
            AppLanguage.ENGLISH to "Hebrew Date"
        ),
        "gregorian_date" to mapOf(
            AppLanguage.INDONESIAN to "Tanggal Gregorian",
            AppLanguage.HEBREW to "תאריך לועזי",
            AppLanguage.ENGLISH to "Gregorian Date"
        ),
        "jewish_holidays" to mapOf(
            AppLanguage.INDONESIAN to "Hari Raya & Peringatan Yahudi",
            AppLanguage.HEBREW to "חגים ומועדי ישראל",
            AppLanguage.ENGLISH to "Jewish Holidays & Observances"
        ),
        "upcoming_holidays" to mapOf(
            AppLanguage.INDONESIAN to "Hari Raya Mendatang",
            AppLanguage.HEBREW to "החגים הקרובים",
            AppLanguage.ENGLISH to "Upcoming Holidays"
        ),
        "no_holidays_today" to mapOf(
            AppLanguage.INDONESIAN to "Tidak ada perayaan hari besar pada tanggal ini",
            AppLanguage.HEBREW to "אין חג מיוחד בתאריך זה",
            AppLanguage.ENGLISH to "No major holiday on this date"
        ),
        "activities_title" to mapOf(
            AppLanguage.INDONESIAN to "Rencana Kegiatan",
            AppLanguage.HEBREW to "לוח פעילויות ומשימות",
            AppLanguage.ENGLISH to "Activities & Plans"
        ),
        "no_activities" to mapOf(
            AppLanguage.INDONESIAN to "Belum ada rencana kegiatan untuk tanggal ini",
            AppLanguage.HEBREW to "אין תוכניות מתוכננות לתאריך זה",
            AppLanguage.ENGLISH to "No activities planned for this date"
        ),
        "add_activity" to mapOf(
            AppLanguage.INDONESIAN to "Tambah Rencana",
            AppLanguage.HEBREW to "הוסף פעילות",
            AppLanguage.ENGLISH to "Add Activity"
        ),
        "edit_activity" to mapOf(
            AppLanguage.INDONESIAN to "Edit Kegiatan",
            AppLanguage.HEBREW to "ערוך פעילות",
            AppLanguage.ENGLISH to "Edit Activity"
        ),
        "activity_title_hint" to mapOf(
            AppLanguage.INDONESIAN to "Judul Kegiatan (misal: Persiapan Shabbat)",
            AppLanguage.HEBREW to "כותרת הפעילות (לדוגמה: הכנות לשבת)",
            AppLanguage.ENGLISH to "Activity title (e.g. Shabbat Preparation)"
        ),
        "activity_desc_hint" to mapOf(
            AppLanguage.INDONESIAN to "Catatan / Keterangan tambahan (opsional)",
            AppLanguage.HEBREW to "הערות ופרטים נוספים (רשות)",
            AppLanguage.ENGLISH to "Notes or extra details (optional)"
        ),
        "time_hint" to mapOf(
            AppLanguage.INDONESIAN to "Waktu (contoh: 18:30 atau kosongkan)",
            AppLanguage.HEBREW to "שעה (למשל: 18:30)",
            AppLanguage.ENGLISH to "Time (e.g. 18:30 or leave blank)"
        ),
        "category" to mapOf(
            AppLanguage.INDONESIAN to "Kategori",
            AppLanguage.HEBREW to "קטגוריה",
            AppLanguage.ENGLISH to "Category"
        ),
        "cat_holiday" to mapOf(
            AppLanguage.INDONESIAN to "Hari Raya & Perayaan",
            AppLanguage.HEBREW to "חג ומועד",
            AppLanguage.ENGLISH to "Holiday & Celebration"
        ),
        "cat_religious" to mapOf(
            AppLanguage.INDONESIAN to "Ibadah & Doa",
            AppLanguage.HEBREW to "תפילה ומצוות",
            AppLanguage.ENGLISH to "Prayer & Religious"
        ),
        "cat_family" to mapOf(
            AppLanguage.INDONESIAN to "Keluarga & Acara",
            AppLanguage.HEBREW to "משפחה ואירועים",
            AppLanguage.ENGLISH to "Family & Social"
        ),
        "cat_work" to mapOf(
            AppLanguage.INDONESIAN to "Pekerjaan",
            AppLanguage.HEBREW to "עבודה",
            AppLanguage.ENGLISH to "Work"
        ),
        "cat_personal" to mapOf(
            AppLanguage.INDONESIAN to "Pribadi",
            AppLanguage.HEBREW to "אישי",
            AppLanguage.ENGLISH to "Personal"
        ),
        "enable_reminder" to mapOf(
            AppLanguage.INDONESIAN to "Nyalakan Pengingat Notifikasi",
            AppLanguage.HEBREW to "הפעל התראת תזכורת",
            AppLanguage.ENGLISH to "Enable Notification Reminder"
        ),
        "save" to mapOf(
            AppLanguage.INDONESIAN to "Simpan",
            AppLanguage.HEBREW to "שמור",
            AppLanguage.ENGLISH to "Save"
        ),
        "cancel" to mapOf(
            AppLanguage.INDONESIAN to "Batal",
            AppLanguage.HEBREW to "ביטול",
            AppLanguage.ENGLISH to "Cancel"
        ),
        "delete" to mapOf(
            AppLanguage.INDONESIAN to "Hapus",
            AppLanguage.HEBREW to "מחק",
            AppLanguage.ENGLISH to "Delete"
        ),
        "filter_all" to mapOf(
            AppLanguage.INDONESIAN to "Semua",
            AppLanguage.HEBREW to "הכל",
            AppLanguage.ENGLISH to "All"
        ),
        "filter_major" to mapOf(
            AppLanguage.INDONESIAN to "Hari Raya Utama",
            AppLanguage.HEBREW to "חגים עיקריים",
            AppLanguage.ENGLISH to "Major Holidays"
        ),
        "filter_fast" to mapOf(
            AppLanguage.INDONESIAN to "Hari Puasa",
            AppLanguage.HEBREW to "צומות",
            AppLanguage.ENGLISH to "Fast Days"
        ),
        "filter_minor" to mapOf(
            AppLanguage.INDONESIAN to "Peringatan & Lainnya",
            AppLanguage.HEBREW to "מועדים נוספים",
            AppLanguage.ENGLISH to "Minor & Observances"
        ),
        "search_holidays_hint" to mapOf(
            AppLanguage.INDONESIAN to "Cari nama hari raya...",
            AppLanguage.HEBREW to "חפש חג או מועד...",
            AppLanguage.ENGLISH to "Search holidays..."
        ),
        "settings_language_title" to mapOf(
            AppLanguage.INDONESIAN to "Pilihan Bahasa Tampilan",
            AppLanguage.HEBREW to "שפת הממשק",
            AppLanguage.ENGLISH to "Display Language"
        ),
        "settings_language_desc" to mapOf(
            AppLanguage.INDONESIAN to "Mendukung Bahasa Indonesia, עברית (Ibrani dengan tata letak RTL), dan English.",
            AppLanguage.HEBREW to "תמיכה מלאה בעברית מימין לשמאל, אינדונזית ואנגלית.",
            AppLanguage.ENGLISH to "Supports Indonesian, Hebrew (RTL layout), and English."
        ),
        "settings_notifications_title" to mapOf(
            AppLanguage.INDONESIAN to "Pengingat & Notifikasi Otomatis",
            AppLanguage.HEBREW to "התראות ותזכורות אוטומטיות",
            AppLanguage.ENGLISH to "Automatic Notification Reminders"
        ),
        "settings_holiday_notif" to mapOf(
            AppLanguage.INDONESIAN to "Pengingat Hari Raya Yahudi",
            AppLanguage.HEBREW to "תזכורות לחגי ישראל",
            AppLanguage.ENGLISH to "Jewish Holiday Reminders"
        ),
        "settings_holiday_notif_desc" to mapOf(
            AppLanguage.INDONESIAN to "Terima pemberitahuan saat menjelang hari raya agar tidak melewatkannya.",
            AppLanguage.HEBREW to "קבל התראה לפני כניסת החג כדי לא לפספס אף מועד.",
            AppLanguage.ENGLISH to "Receive notifications on the eve of holidays so you never miss them."
        ),
        "settings_activity_notif" to mapOf(
            AppLanguage.INDONESIAN to "Pengingat Kegiatan Terjadwal",
            AppLanguage.HEBREW to "תזכורות לפעילויות מתוזמנות",
            AppLanguage.ENGLISH to "Scheduled Activity Reminders"
        ),
        "settings_activity_notif_desc" to mapOf(
            AppLanguage.INDONESIAN to "Bunyikan notifikasi saat waktu kegiatan tiba.",
            AppLanguage.HEBREW to "קבל התראה כאשר מגיע זמן המשימה שנקבעה.",
            AppLanguage.ENGLISH to "Trigger notifications when activity time arrives."
        ),
        "test_notification" to mapOf(
            AppLanguage.INDONESIAN to "Kirim Tes Notifikasi Sekarang",
            AppLanguage.HEBREW to "שלח התראת בדיקה כעת",
            AppLanguage.ENGLISH to "Send Test Notification Now"
        ),
        "test_notification_success" to mapOf(
            AppLanguage.INDONESIAN to "Notifikasi pengingat berhasil dikirim ke perangkat!",
            AppLanguage.HEBREW to "התראת בדיקה נשלחה בהצלחה למכשיר!",
            AppLanguage.ENGLISH to "Test notification sent successfully to device!"
        ),
        "calendar_info_title" to mapOf(
            AppLanguage.INDONESIAN to "Tentang Kalender Ibrani (Luach)",
            AppLanguage.HEBREW to "אודות הלוח העברי (לוח שנה)",
            AppLanguage.ENGLISH to "About Hebrew Calendar (Luach)"
        ),
        "calendar_info_desc" to mapOf(
            AppLanguage.INDONESIAN to "Kalender Ibrani adalah kalender lunisolar matematis yang menghitung bulan berdasarkan fase bulan dan tahun berdasarkan siklus matahari melalui siklus Metonik 19 tahun dengan tahun kabisat (Adar I & Adar II).",
            AppLanguage.HEBREW to "הלוח העברי הינו לוח שנה ירחי-שמשי מבוסס מחזור מטוני בן 19 שנים, הכולל 7 שנים מעוברות עם חודש אדר נוסף (אדר א׳ ואדר ב׳).",
            AppLanguage.ENGLISH to "The Hebrew Calendar is a lunisolar calendar calculated on a 19-year Metonic cycle, with 7 leap years inserting an intercalary month (Adar I and Adar II) to keep holidays in their proper seasons."
        ),
        "all_plans" to mapOf(
            AppLanguage.INDONESIAN to "Semua Agenda Rencana",
            AppLanguage.HEBREW to "כל התוכניות והמשימות",
            AppLanguage.ENGLISH to "All Planned Agendas"
        ),
        "in_days" to mapOf(
            AppLanguage.INDONESIAN to "dalam %d hari",
            AppLanguage.HEBREW to "בעוד %d ימים",
            AppLanguage.ENGLISH to "in %d days"
        ),
        "days_ago" to mapOf(
            AppLanguage.INDONESIAN to "%d hari lalu",
            AppLanguage.HEBREW to "לפני %d ימים",
            AppLanguage.ENGLISH to "%d days ago"
        ),
        "gematria_label" to mapOf(
            AppLanguage.INDONESIAN to "Gematria / Aksara Ibrani",
            AppLanguage.HEBREW to "אותיות עבריות",
            AppLanguage.ENGLISH to "Hebrew Gematria"
        ),
        "view_details" to mapOf(
            AppLanguage.INDONESIAN to "Lihat Rincian",
            AppLanguage.HEBREW to "הצג פרטים",
            AppLanguage.ENGLISH to "View Details"
        ),
        "close" to mapOf(
            AppLanguage.INDONESIAN to "Tutup",
            AppLanguage.HEBREW to "סגור",
            AppLanguage.ENGLISH to "Close"
        ),
        "about_us" to mapOf(
            AppLanguage.INDONESIAN to "Tentang Kami",
            AppLanguage.HEBREW to "אודותינו",
            AppLanguage.ENGLISH to "About Us"
        ),
        "about_us_desc" to mapOf(
            AppLanguage.INDONESIAN to "Informasi aplikasi, fitur kalender, dan pengembang",
            AppLanguage.HEBREW to "מידע על היישום, אלגוריתם הלוח והפיתוח",
            AppLanguage.ENGLISH to "Application information, calendar features & development"
        ),
        "privacy_policy" to mapOf(
            AppLanguage.INDONESIAN to "Kebijakan Privasi",
            AppLanguage.HEBREW to "מדיניות פרטיות",
            AppLanguage.ENGLISH to "Privacy Policy"
        ),
        "privacy_policy_desc" to mapOf(
            AppLanguage.INDONESIAN to "Komitmen keamanan, privasi data lokal, dan izin perangkat",
            AppLanguage.HEBREW to "הגנת פרטיות, אחסון מקומי ואישורי מערכת",
            AppLanguage.ENGLISH to "Privacy commitment, local data storage & device permissions"
        ),
        "back" to mapOf(
            AppLanguage.INDONESIAN to "Kembali",
            AppLanguage.HEBREW to "חזור",
            AppLanguage.ENGLISH to "Back"
        ),
        "app_version" to mapOf(
            AppLanguage.INDONESIAN to "Versi Aplikasi",
            AppLanguage.HEBREW to "גרסת יישום",
            AppLanguage.ENGLISH to "App Version"
        )
    )
}
