package io.zelbess.grpcandroid.storage

import io.reactivex.Flowable
import io.reactivex.Single
import io.zelbess.grpcandroid.domain.Trip

interface Storage {
    fun saveTrip(trip: Trip): Single<Trip>
    fun followTrip(tripId: Int): Flowable<Trip>
}

