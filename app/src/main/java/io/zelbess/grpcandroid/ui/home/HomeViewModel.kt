package io.zelbess.grpcandroid.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.zelbess.grpcandroid.domain.usecases.FetchFeed
import io.zelbess.grpcandroid.domain.usecases.UserRoleInJourney
import kotlinx.android.synthetic.*

class HomeViewModel(
    private val userRoleInJourney: UserRoleInJourney,
    private val feed: FetchFeed
) : ViewModel() {

    val viewState: MutableLiveData<ViewState> = MutableLiveData()
    private val disposable = CompositeDisposable()

    fun onMessageClicked(journeyId: Int) {
        userRoleInJourney.check(journeyId)
            .subscribe { success, fail ->
                success?.let {
                    when (it) {
                        UserRoleInJourney.Result.DRIVER -> viewState.postValue(ViewState.GoToDriverScreen(journeyId))
                        UserRoleInJourney.Result.PASSENGER -> viewState.postValue(ViewState.GoToPassengerScreen(journeyId))
                        UserRoleInJourney.Result.ERROR -> viewState.postValue(ViewState.ShowError("User not a part of journey"))
                    }
                }
                fail?.let {
                    viewState.postValue(ViewState.ShowError(it.localizedMessage))
                }
            }.let {
                disposable.add(it)
            }

    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    fun loadFeed() {
        feed.fetch()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { success, fail ->
                success?.let { viewState.postValue(ViewState.ShowFeed(it)) }
                fail?.let { viewState.postValue(ViewState.ShowError("Error fetching feed: ${it.localizedMessage}")) }
            }
            .let {
                disposable.add(it)
            }
    }

    sealed class ViewState {
        class ShowFeed(val feedItems: List<FeedItem>) : ViewState()
        class GoToDriverScreen(val journeyId: Int) : ViewState()
        class GoToPassengerScreen(val journeyId: Int) : ViewState()
        class ShowError(val message: String) : ViewState()
    }

}

