package com.elytrastudios.assessment.navigation


sealed class Screen(val route: String) {
    object Breeds : Screen("breeds")
    object Settings : Screen("settings")
    object Users : Screen("users")
    object UserDetails : Screen("userDetails/{userId}") {
        fun createRoute(userId: Int) = "userDetails/$userId"
    }
}
