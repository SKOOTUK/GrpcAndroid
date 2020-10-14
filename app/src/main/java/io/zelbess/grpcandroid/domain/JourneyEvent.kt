package io.zelbess.grpcandroid.domain

data class JourneyEvent(
    val id: Int,
    val type: String,
    val eta: Int
)