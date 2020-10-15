package io.zelbess.grpcandroid.storage

import io.reactivex.Flowable
import io.reactivex.Single
import io.zelbess.grpcandroid.domain.model.Event
import io.zelbess.grpcandroid.domain.model.Journey

interface Storage {
    fun saveEvent(journey: Event): Single<Event>
    fun saveJourney(journey: Journey): Single<Journey>
    fun getEvents(): Single<List<Event>>
    fun getJourneyUpdates(tripId: Int): Flowable<Journey>
    fun getJourney(journeyId: Int): Single<Journey>
}

