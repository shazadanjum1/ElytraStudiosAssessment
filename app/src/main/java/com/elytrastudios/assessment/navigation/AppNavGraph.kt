package com.elytrastudios.assessment.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elytrastudios.assessment.presentation.breeds.DogBreedsScreen
import com.elytrastudios.assessment.presentation.users.UsersScreen
import com.elytrastudios.assessment.presentation.settings.SettingsScreen
import com.elytrastudios.assessment.presentation.users.UserDetailsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Breeds.route,
        modifier = modifier
    ) {
        composable(Screen.Breeds.route) { DogBreedsScreen() }
        composable(Screen.Users.route) { UsersScreen(navController = navController) }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.UserDetails.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            UserDetailsScreen(userId = userId, navController = navController)
        }
    }

}
