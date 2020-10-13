package io.zelbess.grpcandroid.driver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.zelbess.tripupdates.CreateTripRequest
import io.zelbess.tripupdates.RxTripServiceGrpc

const val DRIVER_ID = 111

class DriverViewModel(
    private val tripService: RxTripServiceGrpc.RxTripServiceStub
) : ViewModel() {

    val viewState = MutableLiveData<UiState>()
    private val disposable = CompositeDisposable()

    fun createTrip() {
        tripService.createTrip(
            CreateTripRequest.newBuilder()
                .setDriverId(DRIVER_ID)
                .setInvitedPassengerId(222)
                .build()
        ).subscribe { success, fail ->
            success?.let {
                viewState.postValue(UiState.TripCreated(it.id, DRIVER_ID))
            }
            fail?.let {
                viewState.postValue(UiState.ShowError(it.message ?: "Empty error message"))
            }

        }.let { disposable.add(it) }
    }


    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    sealed class UiState {
        class TripCreated(val tripId: Int, val driverId: Int) : UiState()
        class ShowError(val message: String) : UiState()
    }
}


