package io.zelbess.grpcandroid.ui.driver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.grpc.ManagedChannel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.zelbess.grpcandroid.config.DRIVER_ID
import io.zelbess.grpcandroid.storage.Storage
import io.zelbess.tripupdates.CreateTripRequest
import io.zelbess.tripupdates.RxTripServiceGrpc


class DriverViewModel(
    private val managedChannel: ManagedChannel,
    private val storage: Storage
) : ViewModel() {
    private val tripService: RxTripServiceGrpc.RxTripServiceStub by lazy {
        RxTripServiceGrpc.newRxStub(managedChannel)
    }

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
                    followJourneyUpdates(it.id)
                }
                , {
                    viewState.postValue(UiState.ShowError(it.localizedMessage ?: "Oj"))
                }).let { disposable.add(it) }
    }

    private fun followJourneyUpdates(id: Int) {
        storage.getJourneyUpdates(id)
            .subscribe(
                {
                    viewState.postValue(UiState.TripUpdate(it.journeyId, "ETA: ${it.eta}"))
                },
                {
                    viewState.postValue(UiState.ShowError(it.localizedMessage ?: "Uj"))
                }
            ).let { disposable.add(it) }
    }

    override fun onCleared() {
        disposable.clear()
        managedChannel.shutdown()
        super.onCleared()
    }

    sealed class UiState {
        class TripCreated(val tripId: Int, val driverId: Int) : UiState()
        class TripUpdate(val tripId: Int, val message: String) : UiState()
        class ShowError(val message: String) : UiState()
    }
}


