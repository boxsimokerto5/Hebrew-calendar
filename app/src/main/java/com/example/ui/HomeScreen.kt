package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.localization.StringResources
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.HolidaysScreen
import com.example.ui.screens.PlannerScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.SettingsScreen

enum class SubScreen {
    NONE, ABOUT_US, PRIVACY_POLICY
}

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var activeSubScreen by rememberSaveable { mutableStateOf(SubScreen.NONE) }

    // RTL for Hebrew, LTR for Indonesian/English
    val layoutDirection = if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0288D1),
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0 && activeSubScreen == SubScreen.NONE,
                        onClick = {
                            selectedTab = 0
                            activeSubScreen = SubScreen.NONE
                        },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                        label = {
                            Text(
                                StringResources.get("tab_calendar", language),
                                fontWeight = if (selectedTab == 0 && activeSubScreen == SubScreen.NONE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0288D1),
                            selectedTextColor = Color(0xFF0288D1),
                            indicatorColor = Color(0xFFE1F5FE),
                            unselectedIconColor = Color(0xFF90A4AE),
                            unselectedTextColor = Color(0xFF90A4AE)
                        ),
                        modifier = Modifier.testTag("nav_item_calendar")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1 && activeSubScreen == SubScreen.NONE,
                        onClick = {
                            selectedTab = 1
                            activeSubScreen = SubScreen.NONE
                        },
                        icon = { Icon(Icons.Default.Celebration, contentDescription = null) },
                        label = {
                            Text(
                                StringResources.get("tab_holidays", language),
                                fontWeight = if (selectedTab == 1 && activeSubScreen == SubScreen.NONE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFF57C00),
                            selectedTextColor = Color(0xFFF57C00),
                            indicatorColor = Color(0xFFFFF3E0),
                            unselectedIconColor = Color(0xFF90A4AE),
                            unselectedTextColor = Color(0xFF90A4AE)
                        ),
                        modifier = Modifier.testTag("nav_item_holidays")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2 && activeSubScreen == SubScreen.NONE,
                        onClick = {
                            selectedTab = 2
                            activeSubScreen = SubScreen.NONE
                        },
                        icon = { Icon(Icons.Default.EventNote, contentDescription = null) },
                        label = {
                            Text(
                                StringResources.get("tab_planner", language),
                                fontWeight = if (selectedTab == 2 && activeSubScreen == SubScreen.NONE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0288D1),
                            selectedTextColor = Color(0xFF0288D1),
                            indicatorColor = Color(0xFFE1F5FE),
                            unselectedIconColor = Color(0xFF90A4AE),
                            unselectedTextColor = Color(0xFF90A4AE)
                        ),
                        modifier = Modifier.testTag("nav_item_planner")
                    )

                    NavigationBarItem(
                        selected = (selectedTab == 3) || activeSubScreen != SubScreen.NONE,
                        onClick = {
                            selectedTab = 3
                            activeSubScreen = SubScreen.NONE
                        },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = {
                            Text(
                                StringResources.get("tab_settings", language),
                                fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF43A047),
                            selectedTextColor = Color(0xFF43A047),
                            indicatorColor = Color(0xFFE8F5E9),
                            unselectedIconColor = Color(0xFF90A4AE),
                            unselectedTextColor = Color(0xFF90A4AE)
                        ),
                        modifier = Modifier.testTag("nav_item_settings")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(Color(0xFFF6F8FA))
            ) {
                when (activeSubScreen) {
                    SubScreen.ABOUT_US -> {
                        AboutUsScreen(
                            language = language,
                            onBack = { activeSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.PRIVACY_POLICY -> {
                        PrivacyPolicyScreen(
                            language = language,
                            onBack = { activeSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.NONE -> {
                        when (selectedTab) {
                            0 -> CalendarScreen(viewModel = viewModel)
                            1 -> HolidaysScreen(
                                viewModel = viewModel,
                                onNavigateToDate = { date ->
                                    viewModel.selectDate(date)
                                    selectedTab = 0
                                }
                            )
                            2 -> PlannerScreen(
                                viewModel = viewModel,
                                onNavigateToCalendar = { selectedTab = 0 }
                            )
                            3 -> SettingsScreen(
                                viewModel = viewModel,
                                onNavigateToAboutUs = { activeSubScreen = SubScreen.ABOUT_US },
                                onNavigateToPrivacyPolicy = { activeSubScreen = SubScreen.PRIVACY_POLICY }
                            )
                        }
                    }
                }
            }
        }
    }
}
