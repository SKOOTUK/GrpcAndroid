package io.zelbess.grpcandroid.storage

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class History<Type>(defaultObject: Type) {

    private val history: BehaviorSubject<Type> = BehaviorSubject.createDefault(defaultObject)

    fun update(data: Type) {
        history.onNext(data)
    }

    fun getUpdates(): Flowable<Type> = history.toFlowable(BackpressureStrategy.BUFFER)

    fun value(): Single<Type> {
        return Single.just(history.value)
    }

}