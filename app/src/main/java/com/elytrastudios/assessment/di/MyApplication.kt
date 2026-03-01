package com.elytrastudios.assessment.di
import android.app.Application
import com.elytrastudios.assessment.BuildConfig
import com.elytrastudios.assessment.util.LoggingController
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.FLAVOR != "prod") {
            Timber.plant(Timber.DebugTree())
        }
        LoggingController.setEnabled(BuildConfig.ENABLE_LOGGING)
    }
}
