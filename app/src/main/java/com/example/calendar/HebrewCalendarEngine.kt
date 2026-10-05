package com.example.calendar

import android.icu.util.Calendar
import android.icu.util.HebrewCalendar
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

object HebrewCalendarEngine {

    fun isLeapYear(hebrewYear: Int): Boolean {
        return (7 * hebrewYear + 1) % 19 < 7
    }

    fun toGematria(number: Int): String {
        if (number <= 0) return number.toString()
        val units = arrayOf("", "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט")
        val tens = arrayOf("", "י", "כ", "ל", "מ", "נ", "ס", "ע", "פ", "צ")

        if (number == 15) return "ט״ו"
        if (number == 16) return "ט״ז"

        val t = (number / 10) % 10
        val u = number % 10

        val raw = StringBuilder()
        if (t > 0 && t < tens.size) raw.append(tens[t])
        if (u > 0 && u < units.size) raw.append(units[u])

        val str = raw.toString()
        return when (str.length) {
            1 -> "$str׳"
            else -> {
                if (str.length >= 2) {
                    str.substring(0, str.length - 1) + "״" + str.substring(str.length - 1)
                } else {
                    str
                }
            }
        }
    }

    fun toHebrewYearString(year: Int): String {
        val hundredsMap = mapOf(
            100 to "ק", 200 to "ר", 300 to "ש", 400 to "ת",
            500 to "תק", 600 to "תר", 700 to "תש", 800 to "תת"
        )
        val remainder = year % 1000
        val h = (remainder / 100) * 100
        val t = (remainder % 100) / 10
        val u = remainder % 10

        val tens = arrayOf("", "י", "כ", "ל", "מ", "נ", "ס", "ע", "פ", "צ")
        val units = arrayOf("", "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט")

        val sb = StringBuilder()
        hundredsMap[h]?.let { sb.append(it) }

        val lastTwo = remainder % 100
        if (lastTwo == 15) {
            sb.append("טו")
        } else if (lastTwo == 16) {
            sb.append("טז")
        } else {
            if (t > 0 && t < tens.size) sb.append(tens[t])
            if (u > 0 && u < units.size) sb.append(units[u])
        }

        val raw = sb.toString()
        return if (raw.length >= 2) {
            raw.substring(0, raw.length - 1) + "״" + raw.substring(raw.length - 1)
        } else if (raw.length == 1) {
            "$raw׳"
        } else {
            year.toString()
        }
    }

    fun getMonthNames(monthCode: Int, isLeap: Boolean): Triple<String, String, String> {
        // Returns Triple(Hebrew, Indonesian, English)
        return when (monthCode) {
            HebrewCalendar.TISHRI -> Triple("תִּשְׁרֵי", "Tishrei", "Tishrei")
            HebrewCalendar.HESHVAN -> Triple("מַרְחֶשְׁוָן", "Khesywan", "Cheshvan")
            HebrewCalendar.KISLEV -> Triple("כִּסְלֵו", "Kislew", "Kislev")
            HebrewCalendar.TEVET -> Triple("טֵבֵת", "Tewet", "Tevet")
            HebrewCalendar.SHEVAT -> Triple("שְׁבָט", "Syewat", "Shevat")
            HebrewCalendar.ADAR_1 -> Triple("אֲדָר א׳", "Adar I", "Adar I")
            HebrewCalendar.ADAR -> {
                if (isLeap) Triple("אֲדָר ב׳", "Adar II", "Adar II")
                else Triple("אֲדָר", "Adar", "Adar")
            }
            HebrewCalendar.NISAN -> Triple("נִיסָן", "Nisan", "Nisan")
            HebrewCalendar.IYAR -> Triple("אִיָּיר", "Iyyar", "Iyyar")
            HebrewCalendar.SIVAN -> Triple("סִיוָן", "Siwan", "Sivan")
            HebrewCalendar.TAMUZ -> Triple("תַּמּוּז", "Tamuz", "Tammuz")
            HebrewCalendar.AV -> Triple("אָב", "Av", "Av")
            HebrewCalendar.ELUL -> Triple("אֱלוּל", "Elul", "Elul")
            else -> Triple("חודש", "Bulan", "Month")
        }
    }

    fun fromLocalDate(localDate: LocalDate): HebrewDate {
        val cal = HebrewCalendar()
        cal.clear()
        cal.set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)

        val hYear = cal.get(Calendar.YEAR)
        val hMonth = cal.get(Calendar.MONTH)
        val hDay = cal.get(Calendar.DATE)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val leap = isLeapYear(hYear)

        val names = getMonthNames(hMonth, leap)

        return HebrewDate(
            year = hYear,
            monthCode = hMonth,
            monthNameHe = names.first,
            monthNameId = names.second,
            monthNameEn = names.third,
            day = hDay,
            gregorianDate = localDate,
            isLeapYear = leap,
            dayOfWeek = dayOfWeek
        )
    }

    fun toLocalDate(hebrewYear: Int, hebrewMonth: Int, hebrewDay: Int): LocalDate {
        val cal = HebrewCalendar()
        cal.clear()
        cal.set(Calendar.YEAR, hebrewYear)
        cal.set(Calendar.MONTH, hebrewMonth)
        cal.set(Calendar.DATE, hebrewDay)

        val date = Date(cal.timeInMillis)
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    val allHolidaysCatalog: List<JewishHoliday> = listOf(
        // Tishrei
        JewishHoliday(
            id = "rosh_hashanah",
            nameHe = "רֹאשׁ הַשָּׁנָה",
            nameId = "Rosh Hashanah (Tahun Baru)",
            nameEn = "Rosh Hashanah (New Year)",
            category = HolidayCategory.MAJOR_YOM_TOV,
            monthCode = HebrewCalendar.TISHRI,
            dayStart = 1,
            dayEnd = 2,
            descriptionHe = "ראש השנה היהודי, יום תרועה וזיכרון, יום הדין לברואי עולם.",
            descriptionId = "Tahun Baru Yahudi, hari peniupan Shofar dan peringatan penciptaan dunia.",
            descriptionEn = "Jewish New Year, Day of Judgment and sounding of the Shofar horn.",
            greetingHe = "שָׁנָה טוֹבָה וּמְתוּקָה!",
            greetingId = "Shanah Tovah Umetukah! (Tahun yang baik dan manis)",
            greetingEn = "Shanah Tovah! Have a good and sweet new year!",
            traditionsHe = "תקיעת שופר, אכילת תפוח בדבש, רימון, ראש של דג, תשליך.",
            traditionsId = "Peniupan Shofar, makan apel dicelup madu, delima, kepala ikan, dan doa Tashlich di tepi air.",
            traditionsEn = "Blowing the Shofar, dipping apples in honey, pomegranates, Tashlich prayer.",
            isWorkProhibited = true
        ),
        JewishHoliday(
            id = "tzom_gedaliah",
            nameHe = "צוֹם גְּדַלְיָה",
            nameId = "Puasa Gedalya (Tzom Gedaliah)",
            nameEn = "Fast of Gedaliah",
            category = HolidayCategory.FAST_DAY,
            monthCode = HebrewCalendar.TISHRI,
            dayStart = 3,
            dayEnd = 3,
            descriptionHe = "צום לזכר הירצחו של גדליה בן אחיקם, מושל יהודה האחרון לאחר חורבן בית ראשון.",
            descriptionId = "Hari puasa memperingati pembunuhan Gedalya, gubernur terakhir Yudea pasca kehancuran Bait Suci Pertama.",
            descriptionEn = "Fast day mourning the assassination of Gedaliah, the governor of Judea after the First Temple destruction.",
            greetingHe = "צוֹם קַל וּמוֹעִיל",
            greetingId = "Tzom Kal (Semoga puasa lancar)",
            greetingEn = "May you have an easy and meaningful fast",
            traditionsHe = "צום מעלות השחר ועד צאת הכוכבים, תפילות סליחות.",
            traditionsId = "Puasa dari fajar hingga petang, doa Selichot.",
            traditionsEn = "Fast from dawn until nightfall, Selichot prayers."
        ),
        JewishHoliday(
            id = "yom_kippur",
            nameHe = "יוֹם כִּפּוּר",
            nameId = "Yom Kippur (Hari Pendamaian)",
            nameEn = "Yom Kippur (Day of Atonement)",
            category = HolidayCategory.MAJOR_YOM_TOV,
            monthCode = HebrewCalendar.TISHRI,
            dayStart = 10,
            dayEnd = 10,
            descriptionHe = "שבת שבתון, היום הקדוש ביותר בשנה לסליחה, כפרה ותשובה.",
            descriptionId = "Hari paling kudus dalam tradisi Yahudi untuk penebusan dosa, pertobatan, dan rekonsiliasi total.",
            descriptionEn = "The holiest day in Judaism dedicated to complete fasting, prayer, repentance, and atonement.",
            greetingHe = "גְּמַר חֲתִימָה טוֹבָה!",
            greetingId = "Gmar Chatimah Tovah! (Semoga dimeteraikan dalam kitab kehidupan)",
            greetingEn = "Gmar Chatimah Tovah! May you be sealed for a good year!",
            traditionsHe = "צום מלא של 25 שעות, לבישת לבן, נעילת שער, תפילת כל נדרי ותקיעת שופר בסיום.",
            traditionsId = "Puasa penuh 25 jam (makan, minum), pakaian serba putih, doa Kol Nidre, dan tiupan Shofar penutup.",
            traditionsEn = "25-hour complete fast, white clothing, Kol Nidre prayer, Neilah service and final Shofar blast.",
            isWorkProhibited = true
        ),
        JewishHoliday(
            id = "sukkot",
            nameHe = "חַג הַסֻּכּוֹת",
            nameId = "Sukkot (Pesta Pondok Daun)",
            nameEn = "Sukkot (Feast of Tabernacles)",
            category = HolidayCategory.MAJOR_YOM_TOV,
            monthCode = HebrewCalendar.TISHRI,
            dayStart = 15,
            dayEnd = 21,
            descriptionHe = "חג האסיף וזמן שמחתנו, ישיבה בסוכה לזכר נדודי בני ישראל במדבר.",
            descriptionId = "Pesta panen dan sukacita, tinggal di dalam pondok dedaunan (Sukkah) memperingati perjalanan di padang gurun.",
            descriptionEn = "Feast of Tabernacles commemorating the 40 years of wandering in the desert; sitting in the Sukkah.",
            greetingHe = "חַג שָׂמֵחַ! מוֹעֲדִים לְשִׂמְחָה!",
            greetingId = "Chag Sameach! (Selamat Hari Raya!)",
            greetingEn = "Chag Sameach! Joyous Festival!",
            traditionsHe = "ישיבה ואכילה בסוכה, נטילת ארבעת המינים: אתרוג, לולב, הדס וערבה.",
            traditionsId = "Tinggal di Sukkah, menggoyangkan 4 Spesies (Arba'at HaMinim: Etrog, Lulav, Hadas, Arava).",
            traditionsEn = "Dwelling in the Sukkah, waving the Four Species (Etrog, Lulav, Myrtle, Willow).",
            isWorkProhibited = true
        ),
        JewishHoliday(
            id = "hoshana_rabbah",
            nameHe = "הוֹשַׁעְנָא רַבָּא",
            nameId = "Hoshana Rabbah (Puncak Sukkot)",
            nameEn = "Hoshana Rabbah",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.TISHRI,
            dayStart = 21,
            dayEnd = 21,
            descriptionHe = "היום השביעי של סוכות, יום חיתום הדין על המים והגשמים לשנה הבאה.",
            descriptionId = "Hari ke-7 Sukkot, penutupan penghakiman akhir musim dan doa curahan air/hujan.",
            descriptionEn = "7th day of Sukkot, marked by special willow beating and completion of judgment.",
            greetingHe = "פִּתְקָא טָבָא!",
            greetingId = "Pitka Tava! (Ketetapan yang baik!)",
            greetingEn = "A good sealing!",
            traditionsHe = "חבטת ערבות, שבע הקפות עם לולב, לימוד תורה כל הלילה.",
            traditionsId = "Memukul ranting Arava (willow), 7 keliling dengan Lulav, belajar kitab sepanjang malam.",
            traditionsEn = "Beating willow branches, seven circuits around the synagogue with the Lulav."
        ),
        JewishHoliday(
            id = "shemini_atzeret_simchat_torah",
            nameHe = "שְׁמִינִי עֲצֶרֶת וְשִׂמְחַת תּוֹרָה",
            nameId = "Shemini Atzeret & Simchat Torah",
            nameEn = "Shemini Atzeret & Simchat Torah",
            category = HolidayCategory.MAJOR_YOM_TOV,
            monthCode = HebrewCalendar.TISHRI,
            dayStart = 22,
            dayEnd = 22,
            descriptionHe = "סיום ופתיחה מחודשת של מחזור קריאת התורה השנתי בשירה וריקודים.",
            descriptionId = "Hari raya penutup Sukkot & sukacita Kitab Taurat, merayakan selesainya pembacaan Taurat setahun dan memulainya kembali dengan tarian gembira.",
            descriptionEn = "Rejoicing of the Torah, celebrating the conclusion and restart of the annual Torah reading cycle with dancing.",
            greetingHe = "חַג שָׂמֵחַ!",
            greetingId = "Chag Sameach!",
            greetingEn = "Chag Sameach!",
            traditionsHe = "ריקודים והקפות עם ספרי תורה, תפילת הגשם, עלייה לתורה של כל הקהל.",
            traditionsId = "Menari mengarak gulungan Taurat (Hakafot), doa memohon hujan, membaca parashah terakhir dan pertama.",
            traditionsEn = "Hakafot dancing with Torah scrolls, prayer for rain (Tefillat Geshem).",
            isWorkProhibited = true
        ),

        // Kislev & Tevet
        JewishHoliday(
            id = "hanukkah",
            nameHe = "חֲנֻכָּה",
            nameId = "Hanukkah (Festival Cahaya)",
            nameEn = "Hanukkah (Festival of Lights)",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.KISLEV,
            dayStart = 25,
            dayEnd = 32, // Spans 8 days into Tevet (handled dynamically)
            descriptionHe = "חג האורים לזכר ניצחון המכבים ונס פך השמן שדלק שמונה ימים במנורת המקדש.",
            descriptionId = "Festival Cahaya selama 8 hari memperingati mukjizat minyak zaitun di Bait Suci dan kemenangan kaum Makabe.",
            descriptionEn = "8-day Festival of Lights commemorating the Maccabean victory and the miracle of the oil that burned for 8 days.",
            greetingHe = "חַג חֲנֻכָּה שָׂמֵחַ!",
            greetingId = "Chag Hanukkah Sameach! (Selamat Hari Hanukkah)",
            greetingEn = "Happy Hanukkah!",
            traditionsHe = "הדלקת נרות חנוכייה בכל ערב, אכילת לביבות (לאטקעס) וסופגניות, משחק בסביבון (דריידל).",
            traditionsId = "Menyalakan lilin Menorah bertambah tiap malam, makan kue Sufganiyot dan Latkes, bermain Dreidel.",
            traditionsEn = "Lighting the Hanukkiah (Menorah) each evening, eating latkes and jelly donuts (sufganiyot), spinning the dreidel."
        ),
        JewishHoliday(
            id = "asara_btevet",
            nameHe = "צוֹם עֲשָׂרָה בְּטֵבֵת",
            nameId = "Puasa 10 Tevet (Asara B'Tevet)",
            nameEn = "Tenth of Tevet",
            category = HolidayCategory.FAST_DAY,
            monthCode = HebrewCalendar.TEVET,
            dayStart = 10,
            dayEnd = 10,
            descriptionHe = "צום לזכר תחילת המצור של נבוכדנאצר מלך בבל על ירושלים שהוביל לחורבן בית ראשון.",
            descriptionId = "Puasa mengenang awal mula pengepungan Yerusalem oleh Nebukadnezar dari Babilonia.",
            descriptionEn = "Fast day commemorating the beginning of the siege of Jerusalem by Nebuchadnezzar of Babylon.",
            greetingHe = "צוֹם מוֹעִיל",
            greetingId = "Tzom Mo'il (Semoga puasa bermakna)",
            greetingEn = "Have a meaningful fast",
            traditionsHe = "צום מעלות השחר ועד צאת הכוכבים, תפילות סליחות.",
            traditionsId = "Puasa fajar hingga petang, doa Selichot.",
            traditionsEn = "Fasting from dawn until dusk, Selichot prayers."
        ),

        // Shevat
        JewishHoliday(
            id = "tu_bishvat",
            nameHe = "ט״וּ בִּשְׁבָט",
            nameId = "Tu Bishvat (Tahun Baru Pohon)",
            nameEn = "Tu Bishvat (New Year for Trees)",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.SHEVAT,
            dayStart = 15,
            dayEnd = 15,
            descriptionHe = "ראש השנה לאילנות, חג הטבע והנטיעות בארץ ישראל.",
            descriptionId = "Tahun Baru bagi Pepohonan dan alam, merayakan hasil bumi dan menanam pohon di Tanah Terjanji.",
            descriptionEn = "New Year for Trees, celebrating nature, ecological awareness, and agricultural fruit tithes.",
            greetingHe = "חַג אִילָנוֹת שָׂמֵחַ!",
            greetingId = "Chag Ilanot Sameach! (Selamat Hari Pohon!)",
            greetingEn = "Happy Tu Bishvat!",
            traditionsHe = "אכילת פירות משבעת המינים: תאנים, תמרים, רימונים, זיתים, ענבים, נטיעת עצים.",
            traditionsId = "Makan buah dari 7 Hasil Bumi (gandum, jelai, anggur, ara, delima, zaitun, korma), menanam bibit pohon.",
            traditionsEn = "Eating fruits of the Seven Species (figs, dates, pomegranates, olives, grapes), planting trees."
        ),

        // Adar
        JewishHoliday(
            id = "taanit_esther",
            nameHe = "תַּעֲנִית אֶסְתֵּר",
            nameId = "Puasa Ester (Ta'anit Esther)",
            nameEn = "Fast of Esther",
            category = HolidayCategory.FAST_DAY,
            monthCode = HebrewCalendar.ADAR,
            dayStart = 13,
            dayEnd = 13,
            descriptionHe = "צום לזכר שלושת ימי הצום של אסתר המלכה ויהודי פרס לפני ביטול גזירת המן.",
            descriptionId = "Hari puasa mengenang Ratu Ester dan umat Yahudi di Persia sebelum menghadap raja demi membatalkan rencana jahat Haman.",
            descriptionEn = "Fast day commemorating the three-day fast of Queen Esther before petitioning King Ahasuerus.",
            greetingHe = "צוֹם מוֹעִיל",
            greetingId = "Tzom Mo'il (Puasa yang membawa berkah)",
            greetingEn = "Have a meaningful fast",
            traditionsHe = "צום מעלות השחר ועד הערב, מתן מחצית השקל לצדקה.",
            traditionsId = "Puasa dari subuh sampai malam, memberikan sedekah setengah syikal (Machatzit HaShekel).",
            traditionsEn = "Fasting from dawn to dusk, giving charity (Machatzit HaShekel)."
        ),
        JewishHoliday(
            id = "purim",
            nameHe = "פּוּרִים",
            nameId = "Purim (Pesta Sukacita Ester)",
            nameEn = "Purim",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.ADAR,
            dayStart = 14,
            dayEnd = 14,
            descriptionHe = "חג הצלת היהודים בממלכת פרס מגזירת ההשמדה של המן הרשע.",
            descriptionId = "Perayaan keselamatan umat Yahudi dari pembantaian di kekaisaran Persia kuno sebagaimana dikisahkan dalam Kitab Ester.",
            descriptionEn = "Joyful holiday celebrating the salvation of the Jewish people from Haman's plot as told in the Book of Esther.",
            greetingHe = "פּוּרִים שָׂמֵחַ!",
            greetingId = "Purim Sameach! (Selamat Hari Purim!)",
            greetingEn = "Happy Purim!",
            traditionsHe = "קריאת מגילת אסתר, משלוח מנות איש לרעהו, מתנות לאביונים, משתה פורים ולבישת תחפושות, אוזני המן.",
            traditionsId = "Membaca Megillat Esther, mengirim bingkisan makanan (Mishloach Manot), sedekah kaum miskin, pesta kostum ceria, makan kue Hamantaschen.",
            traditionsEn = "Reading Megillat Esther, sending food gifts (Mishloach Manot), giving charity, masquerade costumes, eating Hamantaschen triangular cookies."
        ),
        JewishHoliday(
            id = "shushan_purim",
            nameHe = "שׁוּשַׁן פּוּרִים",
            nameId = "Shushan Purim",
            nameEn = "Shushan Purim",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.ADAR,
            dayStart = 15,
            dayEnd = 15,
            descriptionHe = "חג פורים הנחוג בערים מוקפות חומה מימות יהושע בן נון, כדוגמת ירושלים ושושן הבירה.",
            descriptionId = "Purim yang dirayakan di kota-kota berbenteng sejak zaman Yosua, seperti Yerusalem.",
            descriptionEn = "Purim celebration observed in ancient walled cities such as Jerusalem.",
            greetingHe = "פּוּרִים שָׂמֵחַ!",
            greetingId = "Purim Sameach!",
            greetingEn = "Happy Purim!",
            traditionsHe = "קריאת המגילה וסעודת פורים בירושלים.",
            traditionsId = "Pesta dan pembacaan Megillah di Yerusalem.",
            traditionsEn = "Festive meal and Megillah reading in Jerusalem."
        ),

        // Nisan
        JewishHoliday(
            id = "pesach",
            nameHe = "פֶּסַח (חַג הַמַּצּוֹת)",
            nameId = "Pesach (Paskah Yahudi)",
            nameEn = "Passover (Pesach)",
            category = HolidayCategory.MAJOR_YOM_TOV,
            monthCode = HebrewCalendar.NISAN,
            dayStart = 15,
            dayEnd = 21,
            descriptionHe = "חג החירות לזכר יציאת מצרים מעבדות לחירות וקריעת ים סוף.",
            descriptionId = "Hari Raya Pembebasan memperingati keluarnya bangsa Israel dari perbudakan di Mesir dan terbelahnya Laut Merah.",
            descriptionEn = "Festival of Freedom celebrating the Exodus of the Israelites from Egyptian slavery and the crossing of the Red Sea.",
            greetingHe = "פֶּסַח כָּשֵׁר וְשָׂמֵחַ!",
            greetingId = "Pesach Kasher VeSameach! (Selamat Hari Paskah!)",
            greetingEn = "Chag Pesach Kasher VeSameach! Happy Passover!",
            traditionsHe = "ליל הסדר, אכילת מצה ומרור, קריאת ההגדה, איסור חמץ גמור.",
            traditionsId = "Malam Perjamuan Seder, makan roti tak beragi (Matzah) dan sayur pahit (Maror), membaca Kitab Haggadah, menyingkirkan semua ragi (Chametz).",
            traditionsEn = "The Seder night feast, eating Matzah and bitter herbs (Maror), reading the Haggadah, abstaining from leavened foods (Chametz).",
            isWorkProhibited = true
        ),
        JewishHoliday(
            id = "yom_hashoah",
            nameHe = "יוֹם הַזִּכָּרוֹן לַשּׁוֹאָה וְלַגְּבוּרָה",
            nameId = "Yom HaShoah (Peringatan Holocaust)",
            nameEn = "Holocaust Remembrance Day (Yom HaShoah)",
            category = HolidayCategory.MODERN_MEMORIAL,
            monthCode = HebrewCalendar.NISAN,
            dayStart = 27,
            dayEnd = 27,
            descriptionHe = "יום זיכרון לאומי לשישה מיליון היהודים שנרצחו בשואה ולגבורת הלוחמים.",
            descriptionId = "Hari peringatan nasional bagi 6 juta korban Holocaust dan keberanian para pejuang perlawanan Yahudi.",
            descriptionEn = "National memorial day commemorating the 6 million Jewish victims of the Holocaust and Jewish resistance.",
            greetingHe = "יְהִי זִכְרָם בָּרוּךְ",
            greetingId = "Semoga kenangan mereka menjadi berkah",
            greetingEn = "May their memory be a blessing",
            traditionsHe = "צפירת דומייה של שתי דקות, הדלקת ששת נרות זיכרון, טקסי זיכרון.",
            traditionsId = "Bunyi sirene hening cipta 2 menit, menyalakan 6 lilin peringatan, upacara kenegaraan.",
            traditionsEn = "Two-minute siren of silence, lighting 6 memorial candles, solemn ceremonies."
        ),

        // Iyyar
        JewishHoliday(
            id = "yom_hazikaron",
            nameHe = "יוֹם הַזִּכָּרוֹן לַחַלְלֵי מַעַרְכוֹת יִשְׂרָאֵל",
            nameId = "Yom HaZikaron (Peringatan Pahlawan)",
            nameEn = "Yom HaZikaron (Memorial Day)",
            category = HolidayCategory.MODERN_MEMORIAL,
            monthCode = HebrewCalendar.IYAR,
            dayStart = 4,
            dayEnd = 4,
            descriptionHe = "יום זיכרון לחללי מערכות ישראל ולנפגעי פעולות האיבה.",
            descriptionId = "Hari peringatan nasional untuk para prajurit yang gugur demi membela tanah air dan korban teror.",
            descriptionEn = "Israel's Memorial Day for fallen soldiers and victims of terrorism.",
            greetingHe = "יְהִי זִכְרָם בָּרוּךְ",
            greetingId = "Semoga kenangan para pahlawan abadi",
            greetingEn = "May their memory be a blessing",
            traditionsHe = "צפירות זיכרון, עמידת דום, הדלקת נרות זיכרון וביקור בבתי עלמין צבאיים.",
            traditionsId = "Sirene peringatan, hening cipta, ziarah ke makam para pahlawan.",
            traditionsEn = "Memorial sirens, standing in solemn silence, visiting military cemeteries."
        ),
        JewishHoliday(
            id = "yom_haatzmaut",
            nameHe = "יוֹם הָעַצְמָאוּת",
            nameId = "Yom HaAtzmaut (Hari Kemerdekaan)",
            nameEn = "Israel Independence Day",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.IYAR,
            dayStart = 5,
            dayEnd = 5,
            descriptionHe = "חג העצמאות של מדינת ישראל, המציין את הכרזת המדינה בה' באייר תש\"ח.",
            descriptionId = "Hari kemerdekaan Israel memperingati deklarasi berdirinya negara pada 5 Iyyar 5708 (1948).",
            descriptionEn = "Celebration of Israel's Declaration of Independence in 1948.",
            greetingHe = "יוֹם עַצְמָאוּת שָׂמֵחַ!",
            greetingId = "Selamat Hari Kemerdekaan!",
            greetingEn = "Happy Independence Day!",
            traditionsHe = "טקס הדלקת המשואות בהר הרצל, תפילת הלל, מטס חיל האוויר, פיקניקים.",
            traditionsId = "Penyalaan 12 obor di Gunung Herzl, pertunjukan atraksi udara, kumpul keluarga.",
            traditionsEn = "Torch-lighting ceremony, Hallel prayer, aerial flyovers, family picnics and barbecues."
        ),
        JewishHoliday(
            id = "lag_baomer",
            nameHe = "לַ״ג בָּעוֹמֶר",
            nameId = "Lag BaOmer (Hari ke-33 Omer)",
            nameEn = "Lag BaOmer",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.IYAR,
            dayStart = 18,
            dayEnd = 18,
            descriptionHe = "יום ההילולא של רבי שמעון בר יוחאי וסיום המגפה בקרב תלמידי רבי עקיבא.",
            descriptionId = "Hari ke-33 penghitungan Omer, memperingati berhentinya wabah di antara murid Rabbi Akiva dan peringatan Rabbi Shimon bar Yochai.",
            descriptionEn = "33rd day of the Counting of the Omer, commemorating the end of the plague among Rabbi Akiva's disciples.",
            greetingHe = "חַג שָׂמֵחַ!",
            greetingId = "Chag Sameach!",
            greetingEn = "Happy Lag BaOmer!",
            traditionsHe = "הדלקת מדורות, עלייה להר מירון, חתונות ותספורת ראשונה לילדים (חלאקה).",
            traditionsId = "Menyalakan api unggun, ziarah ke Meron, pesta pernikahan, dan cukur rambut pertama anak (Halaka).",
            traditionsEn = "Lighting festive bonfires, pilgrimage to Meron, holding weddings, first haircuts for 3-year-olds (Upsherin)."
        ),
        JewishHoliday(
            id = "yom_yerushalayim",
            nameHe = "יוֹם יְרוּשָׁלַיִם",
            nameId = "Yom Yerushalayim (Hari Yerusalem)",
            nameEn = "Jerusalem Day",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.IYAR,
            dayStart = 28,
            dayEnd = 28,
            descriptionHe = "יום איחוד ירושלim במלחמת ששת הימים ושחרור הכותל המערבי והעיר העתיקה.",
            descriptionId = "Hari persatuan Yerusalem memperingati terhubungnya kembali Kota Tua dan Tembok Ratapan pada Perang Enam Hari 1967.",
            descriptionEn = "Jerusalem Day commemorating the reunification of Jerusalem and access to the Western Wall in 1967.",
            greetingHe = "יוֹם יְרוּשָׁלַיִם שָׂמֵחַ!",
            greetingId = "Selamat Hari Yerusalem!",
            greetingEn = "Happy Jerusalem Day!",
            traditionsHe = "מצעד הדגלים בירושלים, תפילות הודיה, עלייה לעיר העתיקה.",
            traditionsId = "Pawai bendera keliling Yerusalem, doa syukur, kunjungan ke Tembok Ratapan.",
            traditionsEn = "Flag parade through Jerusalem, thanksgiving prayers, festive tours of the Old City."
        ),

        // Sivan
        JewishHoliday(
            id = "shavuot",
            nameHe = "שָׁבוּעוֹת (חַג מַתַּן תּוֹרָה)",
            nameId = "Shavuot (Hari Pemberian Taurat)",
            nameEn = "Shavuot (Feast of Weeks)",
            category = HolidayCategory.MAJOR_YOM_TOV,
            monthCode = HebrewCalendar.SIVAN,
            dayStart = 6,
            dayEnd = 6,
            descriptionHe = "חג מתן תורה בהר סיני, חג הקציר ויום הביכורים.",
            descriptionId = "Pesta Panen Raya dan perayaan penerimaan Sepuluh Firman & Kitab Taurat di Gunung Sinai.",
            descriptionEn = "Festival commemorating the Giving of the Torah at Mount Sinai and the wheat harvest festival.",
            greetingHe = "חַג שָׁבוּעוֹת שָׂמֵחַ!",
            greetingId = "Chag Shavuot Sameach! (Selamat Hari Shavuot!)",
            greetingEn = "Chag Shavuot Sameach!",
            traditionsHe = "תיקון ליל שבועות (לימוד תורה כל הלילה), אכילת מאכלי חלב וגבינות, קריאת מגילת רות.",
            traditionsId = "Belajar Taurat sepanjang malam (Tikkun Leil), makan hidangan olahan susu dan keju, membaca Kitab Rut.",
            traditionsEn = "All-night Torah study (Tikkun Leil Shavuot), eating dairy foods and cheesecake, reading the Book of Ruth.",
            isWorkProhibited = true
        ),

        // Tammuz
        JewishHoliday(
            id = "shiva_asar_btammuz",
            nameHe = "צוֹם שִׁבְעָה עָשָׂר בְּתַמּוּז",
            nameId = "Puasa 17 Tammuz",
            nameEn = "Fast of the 17th of Tammuz",
            category = HolidayCategory.FAST_DAY,
            monthCode = HebrewCalendar.TAMUZ,
            dayStart = 17,
            dayEnd = 17,
            descriptionHe = "צום לזכר הבקעת חומות ירושלים על ידי הרומאים, פותח את ימי 'בין המצרים'.",
            descriptionId = "Puasa memperingati penjebolan tembok kota Yerusalem oleh Romawi sebelum kehancuran Bait Suci, mengawali masa berkabung 3 pekan.",
            descriptionEn = "Fast day mourning the breach of the walls of Jerusalem, beginning the Three Weeks of mourning.",
            greetingHe = "צוֹם מוֹעִיל",
            greetingId = "Semoga puasa bermakna",
            greetingEn = "Have a meaningful fast",
            traditionsHe = "צום מעלות השחר ועד הערב, הימנעות ממוזיקה ושמחות בשלושת השבועות הבאים.",
            traditionsId = "Puasa subuh hingga petang, menghindari pesta dan musik selama 3 pekan berkabung.",
            traditionsEn = "Fast from sunrise to dusk, beginning solemn customs of the Three Weeks period."
        ),

        // Av
        JewishHoliday(
            id = "tisha_bav",
            nameHe = "תִּשְׁעָה בְּאָב",
            nameId = "Tisha B'Av (Hari Duka 9 Av)",
            nameEn = "Tisha B'Av (Ninth of Av)",
            category = HolidayCategory.FAST_DAY,
            monthCode = HebrewCalendar.AV,
            dayStart = 9,
            dayEnd = 9,
            descriptionHe = "יום האבל הלאומי הגדול לזכר חורבן בית המקדש הראשון והשני וטרגדיות העם היהודי.",
            descriptionId = "Hari berkabung nasional paling khidmat mengenang kehancuran Bait Suci Pertama dan Kedua di Yerusalem.",
            descriptionEn = "Major 25-hour fast mourning the destruction of the First and Second Holy Temples in Jerusalem.",
            greetingHe = "צוֹם מוֹעִיל וּנְחָמָה",
            greetingId = "Semoga mendapatkan penghiburan (Nechama)",
            greetingEn = "May you have a meaningful fast and comfort",
            traditionsHe = "צום מלא של 25 שעות, ישיבה על הרצפה, קריאת מגילת איכה וקינות, הימנעות מנעלי עור.",
            traditionsId = "Puasa 25 jam penuh, duduk di lantai tanpa alas kaki kulit, membaca Kitab Ratapan (Eicha) dan doa ratapan (Kinot).",
            traditionsEn = "25-hour complete fast, sitting on low chairs or floor, reading the Book of Lamentations (Eicha) in dim light.",
            isWorkProhibited = true
        ),
        JewishHoliday(
            id = "tu_bav",
            nameHe = "טַ״ו בְּאָב",
            nameId = "Tu B'Av (Hari Kasih Sayang)",
            nameEn = "Tu B'Av (Jewish Day of Love)",
            category = HolidayCategory.MINOR_HOLIDAY,
            monthCode = HebrewCalendar.AV,
            dayStart = 15,
            dayEnd = 15,
            descriptionHe = "חג האהבה והפיוס במסורת ישראל, יום של שידוכים, שמחה וריקודים בכרמים.",
            descriptionId = "Hari kasih sayang, perjodohan, dan rekonsiliasi dalam tradisi kuno di mana para pemuda-pemudi menari di kebun anggur.",
            descriptionEn = "Ancient festival of love, reconciliation, and joyous dancing in the vineyards.",
            greetingHe = "חַג אַהֲבָה שָׂמֵחַ!",
            greetingId = "Selamat Hari Kasih Sayang!",
            greetingEn = "Happy Day of Love!",
            traditionsHe = "לבישת בגדים לבנים פשוטים, חתונות והצעות נישואין.",
            traditionsId = "Mengenakan pakaian putih sederhana, hari favorit melangsungkan lamaran dan pernikahan.",
            traditionsEn = "Wearing simple white garments, favored date for proposals and weddings."
        )
    )

    fun getHolidaysForDate(date: LocalDate): List<HolidayInstance> {
        val hDate = fromLocalDate(date)
        val list = mutableListOf<HolidayInstance>()

        // Check Shabbat (Saturday)
        if (hDate.dayOfWeek == 7) {
            val shabbat = JewishHoliday(
                id = "shabbat_${date}",
                nameHe = "שַׁבָּת קוֹדֶשׁ",
                nameId = "Shabbat Kodesh",
                nameEn = "Shabbat Kodesh",
                category = HolidayCategory.SHABBAT,
                monthCode = hDate.monthCode,
                dayStart = hDate.day,
                dayEnd = hDate.day,
                descriptionHe = "יום המנוחה השבועי והקדוש של העם היהודי, זכר למעשה בראשית.",
                descriptionId = "Hari perhentian mingguan yang kudus, memperingati selesainya penciptaan alam semesta oleh Tuhan.",
                descriptionEn = "The weekly holy day of rest from Friday sunset to Saturday nightfall.",
                greetingHe = "שַׁבָּת שָׁלוֹם!",
                greetingId = "Shabbat Shalom! (Damai di hari Sabat)",
                greetingEn = "Shabbat Shalom!",
                traditionsHe = "הדלקת נרות שבת, קידוש על היין, שתי חלות לחם משנה, סעודות שבת והבדלה.",
                traditionsId = "Penyalaan lilin sebelum matahari terbenam, doa Kiddush dengan anggur, 2 roti Challah, dan Havdalah di akhir Sabat.",
                traditionsEn = "Lighting Shabbat candles, Kiddush blessing over wine, two braided Challah breads, Havdalah at conclusion.",
                isWorkProhibited = true
            )
            list.add(HolidayInstance(shabbat, hDate, date))
        }

        // Check Rosh Chodesh (1st day of any month, or 30th day of preceding month)
        if (hDate.day == 1) {
            val rc = JewishHoliday(
                id = "rosh_chodesh_${hDate.monthCode}",
                nameHe = "רֹאשׁ חֹדֶשׁ ${hDate.monthNameHe}",
                nameId = "Rosh Chodesh ${hDate.monthNameId} (Bulan Baru)",
                nameEn = "Rosh Chodesh ${hDate.monthNameEn} (New Month)",
                category = HolidayCategory.ROSH_CHODESH,
                monthCode = hDate.monthCode,
                dayStart = 1,
                dayEnd = 1,
                descriptionHe = "ראשית החודש העברי, יום של התחדשות ושמחה בתפילת הלל ומוסף.",
                descriptionId = "Awal bulan baru kalender Ibrani, hari pembaruan berkat dan doa Halel.",
                descriptionEn = "New Month festival marking the birth of the new crescent moon with Hallel and Ya'aleh V'Yavo prayers.",
                greetingHe = "חֹדֶשׁ טוֹב וּמְבֹרָךְ!",
                greetingId = "Chodesh Tov! (Bulan Baru yang berkah)",
                greetingEn = "Chodesh Tov! A good new month!",
                traditionsHe = "קריאת ההלל, תפילת יעלה ויבוא, אכילת סעודה נאה.",
                traditionsId = "Doa pujian Halel dan doa syukur Ya'aleh Veyavo.",
                traditionsEn = "Reciting Hallel psalms, Musaf prayer, festive meal."
            )
            list.add(HolidayInstance(rc, hDate, date))
        } else if (hDate.day == 30) {
            val nextMonthCal = HebrewCalendar()
            nextMonthCal.clear()
            nextMonthCal.set(date.year, date.monthValue - 1, date.dayOfMonth)
            nextMonthCal.add(Calendar.DATE, 1)
            val nextMonthCode = nextMonthCal.get(Calendar.MONTH)
            val nextNames = getMonthNames(nextMonthCode, isLeapYear(nextMonthCal.get(Calendar.YEAR)))

            val rc = JewishHoliday(
                id = "rosh_chodesh_eve_${hDate.monthCode}",
                nameHe = "רֹאשׁ חֹדֶשׁ (יום א׳) ${nextNames.first}",
                nameId = "Rosh Chodesh (Hari ke-1) ${nextNames.second}",
                nameEn = "Rosh Chodesh (Day 1) ${nextNames.third}",
                category = HolidayCategory.ROSH_CHODESH,
                monthCode = hDate.monthCode,
                dayStart = 30,
                dayEnd = 30,
                descriptionHe = "היום הראשון מתוך יומיים של ראש חודש הנחוג ביום ה-30 של החודש היוצא.",
                descriptionId = "Hari pertama dari 2 hari perayaan Rosh Chodesh pada hari ke-30 bulan yang sedang berjalan.",
                descriptionEn = "First of two days of Rosh Chodesh observed on the 30th day of a full month.",
                greetingHe = "חֹדֶשׁ טוֹב!",
                greetingId = "Chodesh Tov!",
                greetingEn = "Chodesh Tov!",
                traditionsHe = "קריאת הלל ותפילת יעלה ויבוא.",
                traditionsId = "Doa Halel dan Ya'aleh Veyavo.",
                traditionsEn = "Hallel psalms and festive additions."
            )
            list.add(HolidayInstance(rc, hDate, date))
        }

        // Hanukkah check:
        // Hanukkah starts 25 Kislev and runs 8 days (25, 26, 27, 28, 29, 30? Kislev, then 1, 2, 3 Tevet)
        if (hDate.monthCode == HebrewCalendar.KISLEV && hDate.day >= 25) {
            val dayNum = hDate.day - 25 + 1
            val hanukkah = allHolidaysCatalog.first { it.id == "hanukkah" }
            list.add(HolidayInstance(hanukkah, hDate, date, dayNumberInHoliday = dayNum, totalDays = 8))
        } else if (hDate.monthCode == HebrewCalendar.TEVET && hDate.day <= 3) {
            // Check day number in Tevet
            val kislev30Cal = HebrewCalendar()
            kislev30Cal.clear()
            kislev30Cal.set(Calendar.YEAR, hDate.year)
            kislev30Cal.set(Calendar.MONTH, HebrewCalendar.KISLEV)
            val kislevDays = kislev30Cal.getActualMaximum(Calendar.DATE)
            val daysFromKislev = (kislevDays - 25 + 1)
            val dayNum = daysFromKislev + hDate.day
            if (dayNum in 1..8) {
                val hanukkah = allHolidaysCatalog.first { it.id == "hanukkah" }
                list.add(HolidayInstance(hanukkah, hDate, date, dayNumberInHoliday = dayNum, totalDays = 8))
            }
        }

        // General Catalog check
        for (h in allHolidaysCatalog) {
            if (h.id == "hanukkah") continue // Handled specially above

            // Handle Adar / Adar II adjustments
            val targetMonth = if (hDate.isLeapYear && (h.id == "purim" || h.id == "shushan_purim" || h.id == "taanit_esther")) {
                HebrewCalendar.ADAR // which is Adar II in ICU when isLeapYear
            } else {
                h.monthCode
            }

            if (hDate.monthCode == targetMonth && hDate.day in h.dayStart..h.dayEnd) {
                val dayNum = hDate.day - h.dayStart + 1
                val totalDays = h.dayEnd - h.dayStart + 1
                list.add(HolidayInstance(h, hDate, date, dayNumberInHoliday = dayNum, totalDays = totalDays))
            }
        }

        // Purim Katan in leap year (14 & 15 Adar I)
        if (hDate.isLeapYear && hDate.monthCode == HebrewCalendar.ADAR_1 && (hDate.day == 14 || hDate.day == 15)) {
            val pk = JewishHoliday(
                id = "purim_katan",
                nameHe = "פּוּרִים קָטָן",
                nameId = "Purim Katan (Purim Kecil)",
                nameEn = "Purim Katan (Minor Purim)",
                category = HolidayCategory.MINOR_HOLIDAY,
                monthCode = HebrewCalendar.ADAR_1,
                dayStart = 14,
                dayEnd = 15,
                descriptionHe = "פורים קטן הנחוג בחודש אדר הראשון בשנה מעוברת.",
                descriptionId = "Purim kecil yang dirayakan pada bulan Adar pertama di tahun kabisat.",
                descriptionEn = "Minor Purim observed in the first Adar of a leap year.",
                greetingHe = "פּוּרִים קָטָן שָׂמֵחַ!",
                greetingId = "Purim Katan Sameach!",
                greetingEn = "Happy Purim Katan!"
            )
            list.add(HolidayInstance(pk, hDate, date, dayNumberInHoliday = hDate.day - 14 + 1, totalDays = 2))
        }

        return list
    }

    fun getUpcomingHolidays(fromDate: LocalDate, count: Int = 12): List<HolidayInstance> {
        val results = mutableListOf<HolidayInstance>()
        var curDate = fromDate
        var daysChecked = 0
        val seenHolidayKeys = mutableSetOf<String>()

        while (results.size < count && daysChecked < 370) {
            val holidays = getHolidaysForDate(curDate).filter { it.holiday.category != HolidayCategory.SHABBAT }
            for (h in holidays) {
                val key = "${h.holiday.id}_${h.hebrewDate.year}"
                if (!seenHolidayKeys.contains(key) || h.holiday.category == HolidayCategory.ROSH_CHODESH) {
                    if (h.holiday.category != HolidayCategory.ROSH_CHODESH) {
                        seenHolidayKeys.add(key)
                    }
                    results.add(h)
                    if (results.size >= count) break
                }
            }
            curDate = curDate.plusDays(1)
            daysChecked++
        }
        return results
    }
}
