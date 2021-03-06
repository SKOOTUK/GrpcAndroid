package io.zelbess.grpcandroid.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

const val USER_ID_KEY = "USER_ID"

class GrpcService : Service() {

    private val grpcModel: GrpcModel by inject()
    private val disposable = CompositeDisposable()
    private val binder = GrpcServiceBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val userId = it.getIntExtra(USER_ID_KEY, -1)
            if(userId != -1){
                startSendingLocation(userId)
            }
        }
        return START_NOT_STICKY;
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private fun startSendingLocation(userId: Int) {
        grpcModel.updateUserLocation(userId)
            .subscribe(
                {},
                {
                    Log.e("SERVICE START", it.localizedMessage)
                })
            .let { disposable.add(it) }
    }

    fun followTrip(userId: Int, tripId: Int) {
        grpcModel.followTrip(userId, tripId)
            .subscribe(
                {},
                {
                    Log.e("SERVICE FOLLOW", it.localizedMessage)
                })
            .let { disposable.add(it) }
    }

    inner class GrpcServiceBinder : Binder() {
        fun getService(): GrpcService {
            return this@GrpcService
        }
    }

    override fun stopService(name: Intent?): Boolean {
        disposable.clear()
        grpcModel.closeChannel()
        return super.stopService(name)
    }
}