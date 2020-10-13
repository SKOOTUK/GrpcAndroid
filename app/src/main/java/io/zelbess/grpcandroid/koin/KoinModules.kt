package io.zelbess.grpcandroid.koin

import org.koin.dsl.module

interface OMG {
    fun onYes(): String
}

class OMGImpl : OMG {
    override fun onYes() = "yup yup"

}

val module = module {
    single<OMG> { OMGImpl() }
}