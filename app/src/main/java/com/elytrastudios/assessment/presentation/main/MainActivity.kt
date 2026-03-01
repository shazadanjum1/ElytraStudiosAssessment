package com.elytrastudios.assessment.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.elytrastudios.assessment.navigation.AppNavGraph
import com.elytrastudios.assessment.navigation.BottomNavigationBar
import com.elytrastudios.assessment.ui.theme.ElytraStudiosAssessmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElytraStudiosAssessmentTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.Companion.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier.Companion.padding(innerPadding)
                    )
                }
            }
        }
    }
}