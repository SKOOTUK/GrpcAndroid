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
import io.zelbess.tripupdates.RxTripServiceGrpc
import io.zelbess.tripupdates.StartTripRequest
import kotlinx.android.synthetic.main.activity_client_stream.*

class ClientStreamActivity : AppCompatActivity() {

    lateinit var channel: ManagedChannel
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_stream)

        val tripService = setupStream()

        var requestCount = 0
        stream.setOnClickListener {
            val request = StartTripRequest.newBuilder().setId(requestCount).build()

            tripService.startTrip(request)
                .subscribeOn(Schedulers.io())
                .map { it.trip }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { updates.text = "Request no: ${it.id} message: ${it.message}" },
                    { showMessage("Error! ${it.message}") },
                    { updates.text = "Completed" }
                )
                .let { disposables.add(it) }

            requestCount++
        }
    }

    private fun setupStream(): RxTripServiceGrpc.RxTripServiceStub {
        channel = ManagedChannelBuilder.forAddress(SERVER_HOST_EMULATOR, SERVER_PORT).usePlaintext().build()
        return RxTripServiceGrpc.newRxStub(channel)

    }

    private fun showMessage(message: String) {
        updates.text = message
    }

    override fun onDestroy() {
        channel.shutdown()
        disposables.clear()
        super.onDestroy()
    }
}