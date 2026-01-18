package com.devdiaz.sensu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.devdiaz.sensu.ui.theme.SensuTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devdiaz.sensu.ui.MoodCheckInScreen
import com.devdiaz.sensu.ui.dashboard.MoodDashboardScreen
import com.devdiaz.sensu.ui.history.HistoryScreen
import com.devdiaz.sensu.ui.settings.ReminderSettingsScreen
import com.devdiaz.sensu.ui.streak.StreakCelebrationScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SensuTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                            navController = navController,
                            startDestination = "dashboard",
                            modifier = Modifier.padding()
                    ) {
                        composable("dashboard") {
                            MoodDashboardScreen(
                                modifier = Modifier.padding(innerPadding),
                                onNavigateToScan = { navController.navigate("scan")},
                                onNavigateToReminders = {
                                    navController.navigate("reminder_settings") },
                                onNavigateToHistory = { navController.navigate("history") }
                            )
                        }

                        composable("history") {
                            HistoryScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable("scan") {
                            MoodCheckInScreen(
                                    onNavigateHome = { navController.popBackStack() },
                                    onNavigateToStreak = { streak ->
                                        navController.navigate("streak_celebration/$streak") {
                                            popUpTo("scan") { inclusive = true }
                                        }
                                    }
                            )
                        }
                        composable(
                                "streak_celebration/{streak}",
                                arguments =
                                        listOf(
                                            navArgument("streak") { type = NavType.IntType }
                                        )
                        ) { backStackEntry ->
                            val streak = backStackEntry.arguments?.getInt("streak") ?: 1
                            StreakCelebrationScreen(
                                    streakDays = streak,
                                    onContinue = { navController.popBackStack() },
                                    onDismiss = { navController.popBackStack() }
                            )
                        }

                        composable("reminder_settings") {
                            ReminderSettingsScreen(
                                modifier = Modifier.padding(innerPadding),
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
