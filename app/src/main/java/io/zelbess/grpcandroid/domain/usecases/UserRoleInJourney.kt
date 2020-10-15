package io.zelbess.grpcandroid.domain.usecases

import io.reactivex.Single
import io.zelbess.grpcandroid.storage.Storage
import io.zelbess.grpcandroid.storage.UserState


class UserRoleInJourney(
    private val userState: UserState,
    private val storage: Storage
) {

    fun check(journeyId: Int): Single<Result> {
        return storage.getJourney(journeyId)
            .map {
                when (userState.userId) {
                    it.driverId -> Result.DRIVER
                    it.passengerId -> Result.PASSENGER
                    else -> Result.ERROR
                }
            }
    }


    enum class Result {
        DRIVER, PASSENGER, ERROR
    }

}
