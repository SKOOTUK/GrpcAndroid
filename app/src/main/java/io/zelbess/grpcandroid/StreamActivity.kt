package io.zelbess.grpcandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.zelbess.grpcandroid.server.SERVER_HOST_EMULATOR
import io.zelbess.grpcandroid.server.SERVER_PORT
import io.zelbess.tripupdates.CreateTripRequest
import io.zelbess.tripupdates.FollowTripRequest
import io.zelbess.tripupdates.RxTripServiceGrpc
import kotlinx.android.synthetic.main.activity_client_stream.*

class StreamActivity : AppCompatActivity() {

    lateinit var channel: ManagedChannel
    private lateinit var tripService: RxTripServiceGrpc.RxTripServiceStub
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_stream)

        tripService = setupService()

        createTrip.setOnClickListener { createTrip() }

        follow.setOnClickListener {
            val id = followId.text?.toString()?.toInt() ?: 0
            followTrip(id)
        }

        unfollow.setOnClickListener {
            disposables.dispose()
            followedTripUpdates.text = ""
        }
    }

    private fun setupService(): RxTripServiceGrpc.RxTripServiceStub {
        channel = ManagedChannelBuilder.forAddress(SERVER_HOST_EMULATOR, SERVER_PORT).usePlaintext().build()
        return RxTripServiceGrpc.newRxStub(channel)
    }

    private fun createTrip() {
        tripService.createTrip(CreateTripRequest.newBuilder().build())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                createTripStatus.text = "Trip created with id: ${it.id}"
                followId.setText("${it.id}")
            }.subscribe()
            .let { disposables.add(it) }
    }

    private fun followTrip(id: Int) {
        tripService.followTrip(FollowTripRequest.newBuilder().setId(id).build())
            .subscribeOn(Schedulers.io())
            .map { it.trip }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { followedTripUpdates.text = "Request no: ${it.id} message: ${it.message}" },
                { followedTripUpdates.text = ("Error! ${it.message}") },
                { followedTripUpdates.text = "Completed" }
            )
            .let { disposables.add(it) }
    }

    override fun onDestroy() {
        channel.shutdown()
        disposables.clear()
        super.onDestroy()
    }
}