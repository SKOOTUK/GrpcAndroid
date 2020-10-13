package io.zelbess.grpcandroid.background

import io.reactivex.Flowable
import io.zelbess.tripupdates.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

interface GrpcModel {
    fun updateUserLocation(): Flowable<UpdateLocationReply>
    fun followTrip(userId: Int, tripId: Int): Flowable<Trip>
}

class GrpcModelImpl(private val tripService: RxTripServiceGrpc.RxTripServiceStub) : GrpcModel {

    override fun updateUserLocation(): Flowable<UpdateLocationReply> {
        val updates = Flowable.interval(2, TimeUnit.SECONDS)
            .map {
                UpdateLocationRequest.newBuilder()
                    .setUserId("123")
                    .setLat(Random.nextDouble(100.0))
                    .setLon(Random.nextDouble(100.0))
                    .build()
            }

        return tripService.updateLocation(updates)
    }

    override fun followTrip(userId: Int, tripId: Int): Flowable<Trip> {
      return  tripService
          .followTrip(FollowTripRequest.newBuilder().setId(tripId).build())
          .map { it.trip }
    }
}