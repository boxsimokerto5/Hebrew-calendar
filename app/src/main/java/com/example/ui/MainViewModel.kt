package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendar.HebrewCalendarEngine
import com.example.calendar.HebrewDate
import com.example.calendar.HolidayCategory
import com.example.calendar.HolidayInstance
import com.example.data.AppDatabase
import com.example.data.PlannerEvent
import com.example.data.PlannerRepository
import com.example.localization.AppLanguage
import com.example.localization.PreferencesManager
import com.example.localization.StringResources
import com.example.notification.NotificationHelper
import com.example.notification.NotificationScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class HolidayFilterTab {
    ALL, MAJOR, FASTS, MINOR
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)
    private val repository: PlannerRepository

    init {
        val dao = AppDatabase.getDatabase(application).plannerEventDao()
        repository = PlannerRepository(dao)
        NotificationHelper.createNotificationChannels(application)
    }

    private val _currentLanguage = MutableStateFlow(prefs.language)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    // Calendar view year and month (Gregorian)
    private val _viewYear = MutableStateFlow(LocalDate.now().year)
    val viewYear: StateFlow<Int> = _viewYear.asStateFlow()

    private val _viewMonth = MutableStateFlow(LocalDate.now().monthValue)
    val viewMonth: StateFlow<Int> = _viewMonth.asStateFlow()

    private val _holidayFilter = MutableStateFlow(HolidayFilterTab.ALL)
    val holidayFilter: StateFlow<HolidayFilterTab> = _holidayFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isHolidayNotifEnabled = MutableStateFlow(prefs.isHolidayNotifEnabled)
    val isHolidayNotifEnabled: StateFlow<Boolean> = _isHolidayNotifEnabled.asStateFlow()

    private val _isActivityNotifEnabled = MutableStateFlow(prefs.isActivityNotifEnabled)
    val isActivityNotifEnabled: StateFlow<Boolean> = _isActivityNotifEnabled.asStateFlow()

    // Derived Hebrew date for currently selected date
    val selectedHebrewDate: StateFlow<HebrewDate> = _selectedDate.map { date ->
        HebrewCalendarEngine.fromLocalDate(date)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HebrewCalendarEngine.fromLocalDate(LocalDate.now())
    )

    // Holidays on selected date
    val holidaysOnSelectedDate: StateFlow<List<HolidayInstance>> = _selectedDate.map { date ->
        HebrewCalendarEngine.getHolidaysForDate(date)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HebrewCalendarEngine.getHolidaysForDate(LocalDate.now())
    )

    // Events on selected date
    @OptIn(ExperimentalCoroutinesApi::class)
    val eventsOnSelectedDate: StateFlow<List<PlannerEvent>> = _selectedDate.flatMapLatest { date ->
        val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        repository.getEventsForDate(dateStr)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Dates that have events (for calendar day dots)
    val datesWithEvents: StateFlow<Set<String>> = repository.getDatesWithEvents().map { list ->
        list.toSet()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )

    // All upcoming events
    val allUpcomingEvents: StateFlow<List<PlannerEvent>> = repository.getUpcomingEvents(
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Upcoming holidays catalog for current year
    val upcomingHolidays: StateFlow<List<HolidayInstance>> = _selectedDate.map {
        HebrewCalendarEngine.getUpcomingHolidays(LocalDate.now(), count = 25)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HebrewCalendarEngine.getUpcomingHolidays(LocalDate.now(), count = 25)
    )

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        prefs.language = language
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _viewYear.value = date.year
        _viewMonth.value = date.monthValue
    }

    fun nextMonth() {
        if (_viewMonth.value == 12) {
            _viewMonth.value = 1
            _viewYear.value += 1
        } else {
            _viewMonth.value += 1
        }
    }

    fun prevMonth() {
        if (_viewMonth.value == 1) {
            _viewMonth.value = 12
            _viewYear.value -= 1
        } else {
            _viewMonth.value -= 1
        }
    }

    fun goToToday() {
        val today = LocalDate.now()
        _selectedDate.value = today
        _viewYear.value = today.year
        _viewMonth.value = today.monthValue
    }

    fun setHolidayFilter(tab: HolidayFilterTab) {
        _holidayFilter.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setHolidayNotifEnabled(enabled: Boolean) {
        _isHolidayNotifEnabled.value = enabled
        prefs.isHolidayNotifEnabled = enabled
        if (enabled) {
            NotificationScheduler.scheduleUpcomingHolidayReminders(getApplication(), _currentLanguage.value)
        }
    }

    fun setActivityNotifEnabled(enabled: Boolean) {
        _isActivityNotifEnabled.value = enabled
        prefs.isActivityNotifEnabled = enabled
    }

    fun addPlannerEvent(
        title: String,
        description: String,
        time: String,
        category: String,
        hasReminder: Boolean
    ) {
        viewModelScope.launch {
            val date = _selectedDate.value
            val hDate = HebrewCalendarEngine.fromLocalDate(date)
            val event = PlannerEvent(
                gregorianDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                hebrewDateString = hDate.getFormattedHebrew(_currentLanguage.value),
                title = title.trim(),
                description = description.trim(),
                time = time.trim(),
                category = category,
                hasReminder = hasReminder && _isActivityNotifEnabled.value
            )
            val id = repository.insertEvent(event)
            if (event.hasReminder) {
                NotificationScheduler.scheduleActivityReminder(getApplication(), event.copy(id = id))
            }
        }
    }

    fun toggleEventCompleted(event: PlannerEvent) {
        viewModelScope.launch {
            repository.updateEvent(event.copy(isCompleted = !event.isCompleted))
        }
    }

    fun updatePlannerEvent(event: PlannerEvent) {
        viewModelScope.launch {
            repository.updateEvent(event)
            if (event.hasReminder && _isActivityNotifEnabled.value) {
                NotificationScheduler.scheduleActivityReminder(getApplication(), event)
            }
        }
    }

    fun deletePlannerEvent(event: PlannerEvent) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }

    fun sendTestNotification() {
        val context = getApplication<Application>()
        val lang = _currentLanguage.value
        val title = when (lang) {
            AppLanguage.HEBREW -> "בדיקת התראה: לוח שנה עברי"
            AppLanguage.INDONESIAN -> "Pengingat Kalender Ibrani Aktif!"
            AppLanguage.ENGLISH -> "Hebrew Calendar Reminder Active!"
        }
        val message = when (lang) {
            AppLanguage.HEBREW -> "התזכורות האוטומטיות לחגים ופעילויות מוגדרות ופועלות כראוי. חג שמח!"
            AppLanguage.INDONESIAN -> "Notifikasi otomatis hari raya Yahudi & kegiatan Anda berfungsi dengan sempurna!"
            AppLanguage.ENGLISH -> "Automated holiday and activity notifications are working perfectly!"
        }
        NotificationHelper.showHolidayNotification(
            context = context,
            notificationId = 9999,
            title = title,
            message = message,
            details = StringResources.get("test_notification_success", lang)
        )
    }
}
