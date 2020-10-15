package io.zelbess.grpcandroid.domain.model

data class Journey(
    val journeyId: Int,
    val driverId: Int,
    val passengerId: Int,
    val eta: Int
)