package com.elytrastudios.assessment.presentation.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elytrastudios.assessment.presentation.common.shimmerEffect



@Composable
fun UserDetailsScreen(
    userId: Int?,
    navController: NavHostController,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val state by viewModel.userState.collectAsState()

    // Trigger fetch when userId changes or on lunch of screen
    LaunchedEffect(userId) {
        userId?.let { viewModel.fetchUser(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar with back navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "User Details",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            // Render UI based on state
            when (state) {
                is UserState.Success -> {
                    val user = (state as UserState.Success).user
                    // Card showing user details
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Name: ${user.name}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "City: ${user.address.city}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Phone: ${user.phone}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Website: ${user.website}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                is UserState.Error -> {
                    // Error UI with retry option
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${(state as UserState.Error).message}",
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            userId?.let { viewModel.fetchUser(it) }
                        }) {
                            Text("Retry")
                        }
                    }
                }
                is UserState.Loading -> {
                    // Shimmer placeholder while loading
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .shimmerEffect(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(12.dp)
                        ) { }
                    }
                }
            }
        }
    }
}
