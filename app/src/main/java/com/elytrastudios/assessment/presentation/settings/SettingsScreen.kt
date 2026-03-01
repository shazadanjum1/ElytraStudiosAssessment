package com.elytrastudios.assessment.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elytrastudios.assessment.BuildConfig
import com.elytrastudios.assessment.util.LoggingController
import com.elytrastudios.assessment.util.PollingStatusManager


@Composable
fun SettingsScreen() {
    val isPolling by PollingStatusManager.isPolling.collectAsState()
    val lastFetchTime by PollingStatusManager.lastFetchTime.collectAsState()
    val loggingEnabled by LoggingController.enabled.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Build Variant: ${BuildConfig.FLAVOR}")
        Text("User API Endpoint: ${BuildConfig.USER_API_BASE_URL}")
        Text("Dog Breeds API Endpoint: ${BuildConfig.DOG_API_BASE_URL}")

        // Show logging toggle only for non-production builds
        if (BuildConfig.FLAVOR != "prod") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Logging Enabled")
                Spacer(modifier = Modifier.width(8.dp))
                // Switch bound to LoggingController state
                Switch( checked = loggingEnabled,
                    onCheckedChange = { LoggingController.setEnabled(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Polling Last Fetch Succeeded: $isPolling")
        Text("Polling Last Fetch Time: $lastFetchTime")
    }
}

