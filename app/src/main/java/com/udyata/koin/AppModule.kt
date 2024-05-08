package com.udyata.koin

import io.ktor.http.URLProtocol
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {

    single {
        HttpClientBuilder()
            .protocol(URLProtocol.HTTPS)
            .host("stock-sense.onrender.com")
            .build()
    }


    single { RequestHandler(get()) }
    single<CommonRepository> { CommonRepositoryImpl(get()) }
    single { LocationMapper() }
    single { LocationUseCase(get(), get()) }
    viewModel { MainViewModel(get()) }
}
