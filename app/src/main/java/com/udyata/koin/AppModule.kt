package com.udyata.koin

import com.udyata.koin.UserDetails.UserDetailUseCase
import com.udyata.koin.auth.AuthUseCase
import com.udyata.koin.stock.AddStockUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { SessionManager(androidContext()) }
}

val networkModule = module {
    single {
        HttpClientBuilder(tokenProvider = { get<SessionManager>().jwtToken })
            .protocol(URLProtocol.HTTPS)
            .host(" ")
            .build()
    }

    single { RequestHandler(httpClient = get()) }
}
val repositoryModule = module {
    single<CommonRepository> { CommonRepositoryImpl(requestHandler = get()) }
}
val useCaseModule = module {
    single { LocationMapper() }
    single { UserMapper() }
    single { LocationUseCase(repository = get(), mapper = get()) }
    single { UserDetailUseCase(repository = get(), mapper = get()) }
    single { AuthUseCase(repository = get(),sessionManager = get()) }
    single { AddStockUseCase(repository = get()) }
}
val viewModelModule = module {
    viewModel {
        MainViewModel(locationUseCase = get(), userDetailUseCase = get(),sessionManager = get(),authUseCase = get(), addStockUseCase = get()) }
}
