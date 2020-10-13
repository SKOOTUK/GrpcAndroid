package io.zelbess.grpcandroid.koin

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.zelbess.grpcandroid.background.GrpcModel
import io.zelbess.grpcandroid.background.GrpcModelImpl
import io.zelbess.grpcandroid.driver.DriverViewModel
import io.zelbess.grpcandroid.server.SERVER_HOST_EMULATOR
import io.zelbess.grpcandroid.server.SERVER_PORT
import io.zelbess.tripupdates.RxTripServiceGrpc
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val module = module {

    single<GrpcModel> { GrpcModelImpl(get()) }

    viewModel { DriverViewModel(get()) }
}

val grpcModule = module {

    factory {
        ManagedChannelBuilder
            .forAddress(SERVER_HOST_EMULATOR, SERVER_PORT)
            .usePlaintext()
            .keepAliveWithoutCalls(true)
            .build()
            .let { RxTripServiceGrpc.newRxStub(it) }
    }

}