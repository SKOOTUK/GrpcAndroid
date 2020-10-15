package io.zelbess.grpcandroid.storage

import io.reactivex.Flowable
import io.reactivex.Single
import io.zelbess.grpcandroid.domain.model.Event
import io.zelbess.grpcandroid.domain.model.Journey
import java.lang.IllegalStateException

class LocalStorage : Storage {

    private val events: MutableList<Event> = mutableListOf()
    private val journeys: MutableMap<Int, History<Journey>> = HashMap()

    override fun saveEvent(event: Event): Single<Event> {
        events.add(event)
        return Single.just(event)
    }

    override fun saveJourney(journey: Journey): Single<Journey> {

        when {
            journeys.containsKey(journey.journeyId) -> {
                journeys[journey.journeyId]?.let { it.update(journey) }
            }
            else -> {
                journeys[journey.journeyId] = History(journey)
            }
        }
        return Single.just(journey)
    }

    override fun getEvents(): Single<List<Event>> {
        return Single.just(events.toList())
    }

    override fun getJourneyUpdates(journeyId: Int): Flowable<Journey> {

        return journeys[journeyId]?.let {
            it.getUpdates()
        } ?: Flowable.error(IllegalStateException("No such journey: $journeyId"))
    }

    override fun getJourney(journeyId: Int): Single<Journey> {
        return journeys[journeyId]?.value() ?: Single.error(IllegalStateException("No such journey: $journeyId"))
    }
}