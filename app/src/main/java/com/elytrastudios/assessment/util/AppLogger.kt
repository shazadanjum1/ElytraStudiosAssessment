package com.elytrastudios.assessment.util

import timber.log.Timber


object AppLogger {

    private val loggingEnabled: Boolean get() = LoggingController.enabled.value

    fun d(tag: String, message: String) {
        if (loggingEnabled) {
            Timber.tag(tag).d(message)
        }
    }

    fun i(tag: String, message: String) {
        if (loggingEnabled) {
            Timber.tag(tag).i(message)
        }
    }

    fun w(tag: String, message: String) {
        if (loggingEnabled) {
            Timber.tag(tag).w(message)
        }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (loggingEnabled) {
            Timber.tag(tag).e(throwable, message)
        }
    }
}
