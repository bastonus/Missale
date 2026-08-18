package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LiturgicalGold
import com.example.ui.theme.RubricRed
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val currentDay by viewModel.currentLiturgicalDay.collectAsState()
    val missalItems by viewModel.missalItems.collectAsState()
    val complineItems by viewModel.complineItems.collectAsState()
    val showChants by viewModel.showGregorianChants.collectAsState()
    val displayLanguage by viewModel.displayLanguage.collectAsState()
    val fontScale by viewModel.fontScale.collectAsState()
    val customKyriale by viewModel.customKyriale.collectAsState()

    var showDatePickerDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Missale & Breviarium 1960",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.goToToday() },
                        modifier = Modifier.testTag("today_appbar_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = "Aujourd'hui",
                            tint = LiturgicalGold
                        )
                    }
                    IconButton(
                        onClick = { viewModel.toggleGregorianChants() },
                        modifier = Modifier.testTag("toggle_chants_appbar_button")
                    ) {
                        Icon(
                            imageVector = if (showChants) Icons.Default.MusicNote else Icons.Default.MusicOff,
                            contentDescription = "Basculer partitions",
                            tint = if (showChants) LiturgicalGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.navigationBarsPadding().testTag("bottom_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = selectedTab == AppTab.MISSAL,
                    onClick = { viewModel.setTab(AppTab.MISSAL) },
                    icon = { Icon(Icons.Default.AutoStories, contentDescription = "Messe") },
                    label = { Text("Messe") },
                    modifier = Modifier.testTag("nav_item_missal")
                )
                NavigationBarItem(
                    selected = selectedTab == AppTab.COMPLINE,
                    onClick = { viewModel.setTab(AppTab.COMPLINE) },
                    icon = { Icon(Icons.Default.NightsStay, contentDescription = "Complies") },
                    label = { Text("Complies") },
                    modifier = Modifier.testTag("nav_item_compline")
                )
                NavigationBarItem(
                    selected = selectedTab == AppTab.CALENDAR,
                    onClick = { viewModel.setTab(AppTab.CALENDAR) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendrier") },
                    label = { Text("Calendrier") },
                    modifier = Modifier.testTag("nav_item_calendar")
                )
                NavigationBarItem(
                    selected = selectedTab == AppTab.CHANTS,
                    onClick = { viewModel.setTab(AppTab.CHANTS) },
                    icon = { Icon(Icons.Default.MusicNote, contentDescription = "Chants") },
                    label = { Text("Chants") },
                    modifier = Modifier.testTag("nav_item_chants")
                )
                NavigationBarItem(
                    selected = selectedTab == AppTab.SETTINGS,
                    onClick = { viewModel.setTab(AppTab.SETTINGS) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Paramètres") },
                    label = { Text("Paramètres") },
                    modifier = Modifier.testTag("nav_item_settings")
                )
            }
        },
        floatingActionButton = {
            if (selectedTab == AppTab.MISSAL || selectedTab == AppTab.COMPLINE) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.toggleGregorianChants() },
                    icon = {
                        Icon(
                            imageVector = if (showChants) Icons.Default.MusicNote else Icons.Default.MusicOff,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(if (showChants) "Partitions actives" else "Texte seul")
                    },
                    containerColor = if (showChants) LiturgicalGold else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (showChants) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("chant_toggle_fab")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.MISSAL -> {
                    MissalScreen(
                        day = currentDay,
                        items = missalItems,
                        showChants = showChants,
                        language = displayLanguage,
                        fontScale = fontScale,
                        onPreviousDay = { viewModel.previousDay() },
                        onNextDay = { viewModel.nextDay() },
                        onToday = { viewModel.goToToday() },
                        onOpenDatePicker = { showDatePickerDialog = true }
                    )
                }
                AppTab.COMPLINE -> {
                    ComplineScreen(
                        day = currentDay,
                        items = complineItems,
                        showChants = showChants,
                        language = displayLanguage,
                        fontScale = fontScale,
                        onPreviousDay = { viewModel.previousDay() },
                        onNextDay = { viewModel.nextDay() },
                        onToday = { viewModel.goToToday() },
                        onOpenDatePicker = { showDatePickerDialog = true }
                    )
                }
                AppTab.CALENDAR -> {
                    CalendarScreen(
                        selectedDate = currentDay.date,
                        onSelectDate = { newDate ->
                            viewModel.setSelectedDate(newDate)
                            viewModel.setTab(AppTab.MISSAL)
                        }
                    )
                }
                AppTab.CHANTS -> {
                    ChantsScreen()
                }
                AppTab.SETTINGS -> {
                    SettingsScreen(
                        displayLanguage = displayLanguage,
                        onLanguageChange = { viewModel.setDisplayLanguage(it) },
                        showGregorianChants = showChants,
                        onToggleChants = { viewModel.setShowGregorianChants(it) },
                        fontScale = fontScale,
                        onFontScaleChange = { viewModel.setFontScale(it) },
                        customKyriale = customKyriale,
                        onKyrialeChange = { viewModel.setKyriale(it) }
                    )
                }
            }
        }
    }

    // Material 3 Date Picker Dialog
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDay.date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val localDate = Instant.ofEpochMilli(selectedMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.setSelectedDate(localDate)
                        }
                        showDatePickerDialog = false
                    },
                    modifier = Modifier.testTag("date_picker_confirm")
                ) {
                    Text("Valider")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("Annuler")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
