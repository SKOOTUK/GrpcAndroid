package io.zelbess.grpcandroid.storage

import io.reactivex.Flowable
import io.reactivex.Single
import io.zelbess.grpcandroid.domain.JourneyEvent

interface Storage {
    fun saveTrip(journey: JourneyEvent): Single<JourneyEvent>
    fun followTrip(tripId: Int): Flowable<JourneyEvent>
}

