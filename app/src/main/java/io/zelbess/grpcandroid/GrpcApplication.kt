package io.zelbess.grpcandroid

import android.app.Application
import io.realm.Realm
import io.zelbess.grpcandroid.config.grpcModule
import io.zelbess.grpcandroid.config.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GrpcApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        startKoin {
            androidContext(this@GrpcApplication)
            modules(module, grpcModule)
        }

    }
}