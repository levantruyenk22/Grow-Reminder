package com.example.growreminder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.growreminder.sign_in.AuthViewModel   // ✅ Đã sửa ở đây
import com.example.growreminder.ui.screens.DailyMotivationScreen
import com.example.growreminder.ui.screens.HealthChoiceScreen
import com.example.growreminder.ui.screens.LoginPage
import com.example.growreminder.ui.screens.NewSkillChoiceScreen
import com.example.growreminder.ui.screens.PersonalDevelopmentScreen
import com.example.growreminder.ui.screens.ProfileScreen
import com.example.growreminder.ui.screens.ScheduleListScreen
import com.example.growreminder.ui.screens.ScheduleScreen
import com.example.growreminder.ui.screens.SignupPage
import com.example.growreminder.ui.screens.StudyChoiceScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginPage(navController = navController, authViewModel = authViewModel)
        }

        composable("signup") {
            SignupPage(navController = navController, authViewModel = authViewModel)
        }

        composable("home") {
            DailyMotivationScreen(navController)
        }

        composable("personalDevelopment") {
            PersonalDevelopmentScreen(navController, authViewModel )
        }

        composable("profile") {
            ProfileScreen(navController, authViewModel)
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
    }
}
