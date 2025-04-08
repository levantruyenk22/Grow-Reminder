package com.example.growreminder.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login") {
        composable("Login") {LoginScreen(navController)}
        composable("PhatTrien") { PersonalDevelopmentScreen(navController) }
        composable("Profile") { ProfileScreen(navController)}
    }
}


