package io.zelbess.grpcandroid

import android.app.Application
import io.zelbess.grpcandroid.koin.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GrpcAndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GrpcAndroidApplication)
            modules(module)
        }
    }
}