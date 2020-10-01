package io.zelbess.grpcandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.zelbess.grpcandroid.server.SERVER_HOST_EMULATOR
import io.zelbess.grpcandroid.server.SERVER_PORT
import io.zelbess.grpcprototype.GreeterGrpc
import io.zelbess.grpcprototype.stream.HelloReply
import io.zelbess.grpcprototype.stream.HelloRequest
import io.zelbess.grpcprototype.stream.StreamingGreeterGrpc
import io.zelbess.tripupdates.RxTripServiceGrpc
import io.zelbess.tripupdates.StartTripRequest
import io.zelbess.tripupdates.TripServiceGrpc
import kotlinx.android.synthetic.main.activity_client_stream.*
import kotlinx.coroutines.flow.asFlow
import java.util.*

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
                .doOnNext {
                    updates.text = "Request no: ${it.id} message: ${it.message}"
                }
                .doOnComplete {
                    updates.text = "Completed"
                }
                .doOnError { t -> showMessage("Error! ${t.message}") }
                .subscribe()
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