package com.udyata.koin

import com.udyata.koin.UserDetails.UserDetailUseCase
import io.ktor.http.URLProtocol
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

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
    single { UserMapper() }
    single { LocationUseCase(get(), get()) }
    single { UserDetailUseCase(get(), get()) }
    viewModel { MainViewModel(get(),get()) }
}
