package io.zelbess.grpcandroid.storage.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TripRealm(
    @PrimaryKey var tripId: Int = 0,
    var message: String = ""
) : RealmObject()