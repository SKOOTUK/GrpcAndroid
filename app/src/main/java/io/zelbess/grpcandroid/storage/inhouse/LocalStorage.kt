package io.zelbess.grpcandroid.storage.inhouse

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.zelbess.grpcandroid.domain.JourneyEvent
import io.zelbess.grpcandroid.storage.Storage

class LocalStorage : Storage {

    private val trips: BehaviorSubject<JourneyEvent> = BehaviorSubject.create()

    override fun saveTrip(journey: JourneyEvent): Single<JourneyEvent> {
        trips.onNext(journey)
        return Single.just(journey)
    }

    override fun followTrip(tripId: Int): Flowable<JourneyEvent> {
        return trips.toFlowable(BackpressureStrategy.BUFFER)
            .filter { it.id == tripId }
    }
}