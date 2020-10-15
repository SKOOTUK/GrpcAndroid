package io.zelbess.grpcandroid.config

import io.grpc.ManagedChannelBuilder
import io.zelbess.grpcandroid.domain.usecases.FetchFeed
import io.zelbess.grpcandroid.domain.usecases.UserRoleInJourney
import io.zelbess.grpcandroid.service.GrpcModel
import io.zelbess.grpcandroid.service.GrpcModelImpl
import io.zelbess.grpcandroid.ui.driver.DriverViewModel
import io.zelbess.grpcandroid.storage.Storage
import io.zelbess.grpcandroid.storage.LocalStorage
import io.zelbess.grpcandroid.storage.UserState
import io.zelbess.grpcandroid.ui.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val module = module {

    single { UserState() }
    single<Storage> { LocalStorage() }
    single<GrpcModel> { GrpcModelImpl(get()) }

    single { FetchFeed(get()) }
    single { UserRoleInJourney(get(), get()) }

    viewModel { DriverViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
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