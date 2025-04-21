package com.example.growreminder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.growreminder.ui.screens.DailyMotivationScreen
import com.example.growreminder.ui.screens.HealthChoiceScreen
import com.example.growreminder.ui.screens.LoginScreen
import com.example.growreminder.ui.screens.NewSkillChoiceScreen
import com.example.growreminder.ui.screens.PersonalDevelopmentScreen
import com.example.growreminder.ui.screens.ProfileScreen
import com.example.growreminder.ui.screens.ScheduleListScreen
import com.example.growreminder.ui.screens.ScheduleScreen
import com.example.growreminder.ui.screens.StudyChoiceScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "DailyMotivationScreen"
    ) {
        composable("Login") { LoginScreen(navController) }
        composable("PhatTrien") { PersonalDevelopmentScreen(navController) }
        composable("Profile") { ProfileScreen(navController) }
        composable("DailyMotivationScreen") { DailyMotivationScreen(navController) }

        // Thêm parameterized route cho schedule với tham số taskName
        composable(
            route = "schedule/{taskName}",
            arguments = listOf(navArgument("taskName") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskName = backStackEntry.arguments?.getString("taskName") ?: "Đọc sách"
            ScheduleScreen(navController = navController, taskName = taskName)
        }

        // Giữ lại route gốc để tương thích ngược
        composable("schedule") { ScheduleScreen(navController) }

        composable("personalDevelopment") { PersonalDevelopmentScreen(navController) }
        composable("studyChoice") { StudyChoiceScreen(navController) }
        composable("HealthChoice") { HealthChoiceScreen(navController) }
        composable("NewSkillChoice") { NewSkillChoiceScreen(navController) }
        composable("schedule_list") { ScheduleListScreen(navController) }
        composable("health_choice") { HealthChoiceScreen(navController) }
        composable("skill_choice") { NewSkillChoiceScreen(navController) }
        composable("study_choice") { StudyChoiceScreen(navController) }
    }
}