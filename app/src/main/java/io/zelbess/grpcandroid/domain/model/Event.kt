package io.zelbess.grpcandroid.domain.model

data class Event(
    val journeyId: Int,
    val type: EventType
)

enum class EventType {
    SHARED_DRIVE_INVITE,
    INVITE_ACCEPTED,
    JOURNEY_STARTED,
    JOURNEY_FINISHED
}