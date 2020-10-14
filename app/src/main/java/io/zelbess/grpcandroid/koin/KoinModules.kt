package io.zelbess.grpcandroid.koin

import io.grpc.ManagedChannelBuilder
import io.zelbess.grpcandroid.background.GrpcModel
import io.zelbess.grpcandroid.background.GrpcModelImpl
import io.zelbess.grpcandroid.driver.DriverViewModel
import io.zelbess.grpcandroid.storage.Storage
import io.zelbess.grpcandroid.server.SERVER_HOST_EMULATOR
import io.zelbess.grpcandroid.server.SERVER_PORT
import io.zelbess.grpcandroid.storage.inhouse.LocalStorage
import io.zelbess.tripupdates.RxTripServiceGrpc
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val module = module {

    single<Storage> { LocalStorage() }
    single<GrpcModel> { GrpcModelImpl(get(), get()) }
    viewModel { DriverViewModel(get(), get()) }
}

val grpcModule = module {

    factory {
        ManagedChannelBuilder
            .forAddress(SERVER_HOST_EMULATOR, SERVER_PORT)
            .usePlaintext()
            .keepAliveWithoutCalls(true)
            .maxRetryAttempts(5)
            .build()
    }
}