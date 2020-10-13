package io.zelbess.grpcandroid.background

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.zelbess.grpcandroid.server.SERVER_HOST_EMULATOR
import io.zelbess.grpcandroid.server.SERVER_PORT
import io.zelbess.tripupdates.RxTripServiceGrpc
import io.zelbess.tripupdates.UpdateLocationRequest
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class LocationService : Service() {

    lateinit var channel: ManagedChannel
    private lateinit var tripService: RxTripServiceGrpc.RxTripServiceStub
    private val disposable = CompositeDisposable()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        channel = ManagedChannelBuilder
            .forAddress(SERVER_HOST_EMULATOR, SERVER_PORT)
            .usePlaintext()
            .keepAliveWithoutCalls(true)
            .build()
        tripService = RxTripServiceGrpc.newRxStub(channel)

        startSendingLocation()
        return START_NOT_STICKY;
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun startSendingLocation() {

        val updates = Flowable.interval(2, TimeUnit.SECONDS)
            .map {
                UpdateLocationRequest.newBuilder()
                    .setUserId("123")
                    .setLat(Random.nextDouble(100.0))
                    .setLon(Random.nextDouble(100.0))
                    .build()
            }

        tripService.updateLocation(updates)
            .doOnError { Log.e("WHAT", "WHAT") }
            .subscribe()
            .let { disposable.add(it) }
    }

    override fun stopService(name: Intent?): Boolean {
        disposable.clear()
        return super.stopService(name)
    }
}