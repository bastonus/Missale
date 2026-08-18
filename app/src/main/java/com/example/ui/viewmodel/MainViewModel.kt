package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.liturgy.calendar.LiturgicalCalendar
import com.example.liturgy.data.ComplineData
import com.example.liturgy.data.OrdoMissaeData
import com.example.liturgy.model.KyrialeSetting
import com.example.liturgy.model.LiturgicalDay
import com.example.liturgy.model.LiturgicalItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

enum class AppTab(val title: String) {
    MISSAL("Messe"),
    COMPLINE("Complies"),
    CALENDAR("Calendrier"),
    CHANTS("Chants"),
    SETTINGS("Paramètres")
}

enum class DisplayLanguage(val label: String) {
    BILINGUAL_FRENCH("Latin - Français"),
    BILINGUAL_ENGLISH("Latin - English"),
    LATIN_ONLY("Latin seul")
}

class MainViewModel : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _selectedTab = MutableStateFlow(AppTab.MISSAL)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _showGregorianChants = MutableStateFlow(true)
    val showGregorianChants: StateFlow<Boolean> = _showGregorianChants.asStateFlow()

    private val _displayLanguage = MutableStateFlow(DisplayLanguage.BILINGUAL_FRENCH)
    val displayLanguage: StateFlow<DisplayLanguage> = _displayLanguage.asStateFlow()

    private val _fontScale = MutableStateFlow(1.0f)
    val fontScale: StateFlow<Float> = _fontScale.asStateFlow()

    private val _customKyriale = MutableStateFlow<KyrialeSetting?>(null)
    val customKyriale: StateFlow<KyrialeSetting?> = _customKyriale.asStateFlow()

    val currentLiturgicalDay: StateFlow<LiturgicalDay> = _selectedDate
        .combine(_customKyriale) { date, custom ->
            val day = LiturgicalCalendar.computeDay(date)
            if (custom != null) day.copy(suggestedKyriale = custom) else day
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            LiturgicalCalendar.computeDay(LocalDate.now())
        )

    val missalItems: StateFlow<List<LiturgicalItem>> = currentLiturgicalDay
        .combine(_customKyriale) { day, custom ->
            val kyriale = custom ?: day.suggestedKyriale
            OrdoMissaeData.generateOrdoMissae(day, kyriale)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val complineItems: StateFlow<List<LiturgicalItem>> = currentLiturgicalDay
        .combine(_selectedDate) { day, _ ->
            ComplineData.generateCompline(day)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun goToToday() {
        _selectedDate.value = LocalDate.now()
    }

    fun nextDay() {
        _selectedDate.value = _selectedDate.value.plusDays(1)
    }

    fun previousDay() {
        _selectedDate.value = _selectedDate.value.minusDays(1)
    }

    fun setTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun toggleGregorianChants() {
        _showGregorianChants.value = !_showGregorianChants.value
    }

    fun setShowGregorianChants(show: Boolean) {
        _showGregorianChants.value = show
    }

    fun setDisplayLanguage(lang: DisplayLanguage) {
        _displayLanguage.value = lang
    }

    fun setFontScale(scale: Float) {
        _fontScale.value = scale.coerceIn(0.8f, 1.5f)
    }

    fun setKyriale(setting: KyrialeSetting?) {
        _customKyriale.value = setting
    }
}
