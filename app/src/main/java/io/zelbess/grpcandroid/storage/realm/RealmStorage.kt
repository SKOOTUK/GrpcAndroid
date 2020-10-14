package io.zelbess.grpcandroid.storage.realm

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.toFlowable
import io.realm.Realm
import io.zelbess.grpcandroid.domain.Trip
import io.zelbess.grpcandroid.storage.Storage

class RealmStorage() : Storage {

    override fun saveTrip(trip: Trip): Single<Trip> {

        return Single.just(trip)
            .doOnSuccess {
                Realm.getDefaultInstance().executeTransaction {
                    it.createObject(TripRealm::class.java).apply {
                        tripId = trip.id
                        message = trip.message
                    }
                }
            }
    }

    override fun followTrip(tripId: Int): Flowable<Trip> {
        return Realm.getDefaultInstance().where(TripRealm::class.java)
            .equalTo("tripId", tripId)
            .findAll()
            .asFlowable()
            .filter { it.isLoaded }
            .flatMap { it.toFlowable() }
            .map { Trip(it.tripId, it.message) }
    }
}