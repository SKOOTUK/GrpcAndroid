package io.zelbess.grpcandroid.service

import com.google.protobuf.Timestamp
import io.grpc.ManagedChannel
import io.reactivex.Flowable
import io.zelbess.grpcandroid.domain.model.Event
import io.zelbess.grpcandroid.domain.model.EventType
import io.zelbess.grpcandroid.domain.model.Journey
import io.zelbess.grpcandroid.storage.Storage
import io.zelbess.tripupdates.*
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import kotlin.random.Random

interface GrpcModel {
    fun updateUserLocation(userId: Int): Flowable<UpdateLocationReply>
    fun followTrip(userId: Int, tripId: Int): Flowable<Event>
    fun closeChannel()
}

class GrpcModelImpl(
    private val managedChannel: ManagedChannel
) : GrpcModel {

    private val tripService: RxTripServiceGrpc.RxTripServiceStub by lazy {
        RxTripServiceGrpc.newRxStub(managedChannel)
    }

    override fun updateUserLocation(userId: Int): Flowable<UpdateLocationReply> {

        val updates = Flowable.interval(2, TimeUnit.SECONDS)
            .map {
                UpdateLocationRequest.newBuilder()
                    .setUserId(userId)
                    .setLatitude(Random.nextFloat() * 100)
                    .setLongitude(Random.nextFloat() * 100)
                    .setTime(Timestamp.newBuilder().setSeconds(TimeUnit.MILLISECONDS.toSeconds(DateTime.now().millis)))
                    .build()
            }

        return tripService.updateLocation(updates)
    }

    override fun followTrip(userId: Int, tripId: Int): Flowable<Event> {
        //todo
        return Flowable.empty()
    }

    override fun closeChannel() {
        managedChannel.shutdown()
    }
}