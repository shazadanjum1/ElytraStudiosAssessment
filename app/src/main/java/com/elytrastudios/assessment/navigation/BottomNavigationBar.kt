package com.elytrastudios.assessment.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.util.Locale

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Screen.Breeds, Screen.Users, Screen.Settings)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = {
                    when (screen) {
                        Screen.Breeds -> Icon(Icons.Filled.Home, contentDescription = "Breeds")
                        Screen.Users -> Icon(Icons.Default.Person, contentDescription = "Users")
                        Screen.Settings -> Icon(Icons.Default.Settings, contentDescription = "Settings")
                        else -> Icon(Icons.Filled.Home, contentDescription = "Breeds")
                    }
                },
                label = { Text(screen.route.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString() }) }
            )
        }
    }
}
