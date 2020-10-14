package io.zelbess.grpcandroid.background

import io.grpc.ManagedChannel
import io.reactivex.Flowable
import io.zelbess.grpcandroid.domain.JourneyEvent
import io.zelbess.grpcandroid.driver.DRIVER_ID
import io.zelbess.grpcandroid.storage.Storage
import io.zelbess.tripupdates.FollowTripRequest
import io.zelbess.tripupdates.RxTripServiceGrpc
import io.zelbess.tripupdates.UpdateLocationReply
import io.zelbess.tripupdates.UpdateLocationRequest
import java.util.concurrent.TimeUnit
import kotlin.random.Random

interface GrpcModel {
    fun updateUserLocation(): Flowable<UpdateLocationReply>
    fun followTrip(userId: Int, tripId: Int): Flowable<JourneyEvent>
    fun closeChannel()
}

class GrpcModelImpl(
    private val managedChannel: ManagedChannel,
    private val storage: Storage
) : GrpcModel {

    private val tripService: RxTripServiceGrpc.RxTripServiceStub by lazy {
        RxTripServiceGrpc.newRxStub(managedChannel)
    }

    override fun updateUserLocation(): Flowable<UpdateLocationReply> {

        val updates = Flowable.interval(2, TimeUnit.SECONDS)
            .map {
                UpdateLocationRequest.newBuilder()
                    .setUserId(DRIVER_ID)
                    .setLat(Random.nextDouble(100.0))
                    .setLon(Random.nextDouble(100.0))
                    .build()
            }

        return tripService.updateLocation(updates)
    }

    override fun followTrip(userId: Int, tripId: Int): Flowable<JourneyEvent> {
        return tripService
            .followTrip(FollowTripRequest.newBuilder().setId(tripId).build())
            .map { it.trip.let { JourneyEvent(it.id, it.type, it.eta) } }
            .doOnNext { storage.saveTrip(it) }
    }

    override fun closeChannel() {
        managedChannel.shutdown()
    }
}