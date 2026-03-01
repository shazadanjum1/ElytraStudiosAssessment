package com.elytrastudios.assessment.util

import com.elytrastudios.assessment.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LoggingController {
    private val _enabled = MutableStateFlow(BuildConfig.ENABLE_LOGGING)
    val enabled: StateFlow<Boolean> = _enabled

    fun setEnabled(value: Boolean) {
        _enabled.value = value
    }
}
