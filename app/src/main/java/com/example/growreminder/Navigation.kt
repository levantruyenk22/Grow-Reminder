package com.example.growreminder

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.growreminder.sign_in.AuthViewModel
import com.example.growreminder.ui.components.CustomBottomBar
import com.example.growreminder.ui.screens.*

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    isFromNotification: Boolean = false,
    notificationDestination: String? = null
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // ❌ Các màn hình KHÔNG hiển thị BottomBar
    val noBottomBarRoutes = listOf("login", "signup", "profile")

    // 🔍 Lấy route hiện tại
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 🎯 Màn hình khởi đầu
    val startDestination = "login"

    Scaffold(
        bottomBar = {
            if (noBottomBarRoutes.none { currentRoute?.startsWith(it) == true }) {
                CustomBottomBar(
                    onFabClick = {
                        navController.navigate("schedule")
                    },
                    onNavClick = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginPage(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }

            composable("signup") {
                SignupPage(navController = navController, authViewModel = authViewModel)
            }

            composable("home") {
                DailyMotivationScreen(navController)
            }

            composable("personalDevelopment") {
                PersonalDevelopmentScreen(navController, authViewModel)
            }

            composable("profile") {
                ProfileScreen(navController = navController, authViewModel = authViewModel)
            }

            composable(
                route = "schedule/{taskName}",
                arguments = listOf(navArgument("taskName") { type = NavType.StringType })
            ) { backStackEntry ->
                val taskName = backStackEntry.arguments?.getString("taskName") ?: "Đọc sách"
                ScheduleScreen(navController = navController, taskName = taskName)
            }

            composable("schedule") {
                ScheduleScreen(navController)
            }

            composable("schedule_list") {
                ScheduleListScreen(navController)
            }

            composable("studyChoice") {
                StudyChoiceScreen(navController)
            }

            composable("healthChoice") {
                HealthChoiceScreen(navController)
            }

            composable("newSkillChoice") {
                NewSkillChoiceScreen(navController)
            }

            composable("calendar") {
                ScheduleListScreen(navController)
            }

            composable("docs") {
                PersonalDevelopmentScreen(navController, authViewModel)
            }

            composable("people") {
                ProfileScreen(navController, authViewModel)
            }
        }
    }
}
