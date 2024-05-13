package com.udyata.koin

import com.udyata.koin.UserDetails.UserDetailUseCase
import io.ktor.http.URLProtocol
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { SessionManager(androidContext()) }
}

val networkModule = module {
    single {
        HttpClientBuilder()
            .protocol(URLProtocol.HTTPS)
            .host("stock-sense.onrender.com")
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
}
val viewModelModule = module {
    viewModel { MainViewModel(locationUseCase = get(), userDetailUseCase = get(),sessionManager = get()) }
}
