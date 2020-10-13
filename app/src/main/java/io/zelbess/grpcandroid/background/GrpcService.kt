package io.zelbess.grpcandroid.background

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

class GrpcService : Service() {

    private val grpcModel: GrpcModel by inject()
    private val disposable = CompositeDisposable()
    private val binder = GrpcServiceBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startSendingLocation()
        return START_NOT_STICKY;
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun startSendingLocation() {
        grpcModel.updateUserLocation()
            .doOnError { Log.e("WHAT", "WHAT") }
            .subscribe()
            .let { disposable.add(it) }
    }

    fun followTrip(userId: Int, tripId: Int) {
        grpcModel.followTrip(userId, tripId)
            .subscribe()
            .let { disposable.add(it) }
    }

    inner class GrpcServiceBinder : Binder() {
        fun getService(): GrpcService {
            return this@GrpcService
        }
    }

    override fun stopService(name: Intent?): Boolean {
        disposable.clear()
        return super.stopService(name)
    }
}