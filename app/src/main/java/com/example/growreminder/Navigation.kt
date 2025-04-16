package com.example.growreminder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        startDestination = "personalDevelopment"
    ) {
        composable("Login") { LoginScreen(navController) }
        composable("PhatTrien") { PersonalDevelopmentScreen(navController) }
        composable("Profile") { ProfileScreen(navController) }
        composable("DailyMotivationScreen") { DailyMotivationScreen(navController) }
        composable("schedule") { ScheduleScreen(navController) }
        composable("personalDevelopment") { PersonalDevelopmentScreen(navController) }
        composable("studyChoice") { StudyChoiceScreen(navController) }
        composable("HealthChoice") { HealthChoiceScreen(navController) }
        composable("NewSkillChoice") { NewSkillChoiceScreen(navController) }
        composable("schedule_list") { ScheduleListScreen(navController) }
    }
}