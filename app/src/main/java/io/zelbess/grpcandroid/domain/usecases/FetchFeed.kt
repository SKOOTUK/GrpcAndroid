package io.zelbess.grpcandroid.domain.usecases

import io.reactivex.Single
import io.zelbess.grpcandroid.domain.model.EventType
import io.zelbess.grpcandroid.storage.Storage
import io.zelbess.grpcandroid.ui.home.FeedItem

class FetchFeed(
    private val storage: Storage
) {

    fun fetch(): Single<List<FeedItem>> {
        return storage.getEvents().map {
            it.map {
                val message = when (it.type) {
                    EventType.SHARED_DRIVE_INVITE -> "You have been invited to a shared drive"
                    EventType.INVITE_ACCEPTED -> "You have been invited to a shared drive"
                    EventType.JOURNEY_FINISHED -> "You have arrived!"
                    EventType.JOURNEY_STARTED -> "Your journey has just started!"
                }
                FeedItem(it.journeyId, message)
            }
        }
    }
}