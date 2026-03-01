package com.elytrastudios.assessment.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PollingStatusManager {
    private val _isPolling = MutableStateFlow(false)
    val isPolling: StateFlow<Boolean> = _isPolling

    private val _lastFetchTime = MutableStateFlow("")
    val lastFetchTime: StateFlow<String> = _lastFetchTime

    fun updatePollingStatus(active: Boolean) {
        _isPolling.value = active
    }

    fun updateLastFetchTime(timestamp: String) {
        _lastFetchTime.value = timestamp
    }
}
