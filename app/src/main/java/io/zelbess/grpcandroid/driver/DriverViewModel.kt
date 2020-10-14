package io.zelbess.grpcandroid.driver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.zelbess.grpcandroid.storage.Storage
import io.zelbess.tripupdates.CreateTripRequest
import io.zelbess.tripupdates.RxTripServiceGrpc

const val DRIVER_ID = 111

class DriverViewModel(
    private val tripService: RxTripServiceGrpc.RxTripServiceStub,
    private val storage: Storage
) : ViewModel() {

    val viewState = MutableLiveData<UiState>()
    private val disposable = CompositeDisposable()

    fun createTrip() {
        tripService.createTrip(
            CreateTripRequest.newBuilder()
                .setDriverId(DRIVER_ID)
                .setInvitedPassengerId(222)
                .build()
        )
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                viewState.postValue(UiState.TripCreated(it.id, DRIVER_ID))
            }
            .subscribe(
                {
                    followTripUpdatesFromRealm(it.id)
                }
                , {
                    viewState.postValue(UiState.ShowError(it.localizedMessage ?: "Oj"))
                }).let { disposable.add(it) }
    }

    private fun followTripUpdatesFromRealm(id: Int) {
        storage.followTrip(id)
            .subscribe(
                {
                    viewState.postValue(UiState.TripUpdate(it.id, it.message))
                },
                {
                    viewState.postValue(UiState.ShowError(it.localizedMessage?: "Uj"))
                }
            ).let { disposable.add(it) }
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    sealed class UiState {
        class TripCreated(val tripId: Int, val driverId: Int) : UiState()
        class TripUpdate(val tripId: Int, val message: String) : UiState()
        class ShowError(val message: String) : UiState()
    }
}


