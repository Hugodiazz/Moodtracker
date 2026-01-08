package com.devdiaz.sensu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import com.devdiaz.sensu.ui.theme.SensuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SensuTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = androidx.navigation.compose.rememberNavController()
                    androidx.navigation.compose.NavHost(
                            navController = navController,
                            startDestination = "dashboard",
                            modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("dashboard") {
                            com.devdiaz.sensu.ui.dashboard.MoodDashboardScreen(
                                    onNavigateToScan = { navController.navigate("scan") },
                                    onNavigateToReminders = {
                                        navController.navigate("reminder_settings")
                                    }
                            )
                        }

                        composable("scan") {
                            com.devdiaz.sensu.ui.MoodCheckInScreen(
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
                                                androidx.navigation.navArgument("streak") {
                                                    type = androidx.navigation.NavType.IntType
                                                }
                                        )
                        ) { backStackEntry ->
                            val streak = backStackEntry.arguments?.getInt("streak") ?: 1
                            com.devdiaz.sensu.ui.streak.StreakCelebrationScreen(
                                    streakDays = streak,
                                    onContinue = { navController.popBackStack() },
                                    onDismiss = { navController.popBackStack() }
                            )
                        }

                        composable("reminder_settings") {
                            com.devdiaz.sensu.ui.settings.ReminderSettingsScreen(
                                    onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SensuTheme { Greeting("Android") }
}
