package io.zelbess.grpcandroid.storage.inhouse

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.zelbess.grpcandroid.domain.Trip
import io.zelbess.grpcandroid.storage.Storage

class LocalStorage : Storage {

    private val trips: BehaviorSubject<Trip> = BehaviorSubject.create()

    override fun saveTrip(trip: Trip): Single<Trip> {
        trips.onNext(trip)
        return Single.just(trip)
    }

    override fun followTrip(tripId: Int): Flowable<Trip> {
        return trips.toFlowable(BackpressureStrategy.BUFFER)
            .filter { it.id == tripId }
    }
}