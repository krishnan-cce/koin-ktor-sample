package com.udyata.koin

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class HttpClientBuilder(private val tokenProvider: () -> String) {

    private var protocol: URLProtocol = URLProtocol.HTTP
    private lateinit var host: String
    private var port: Int? = null

    fun protocol(protocol: URLProtocol) = apply { this.protocol = protocol }
    fun host(host: String) = apply { this.host = host }
    fun port(port: Int) = apply { this.port = port }

    fun build(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true


            engine {
                endpoint {
                    keepAliveTime = 5000
                    connectTimeout = 5000
                    connectAttempts = 1
                }
            }

            defaultRequest {
                url {
                    protocol = this@HttpClientBuilder.protocol
                    host = this@HttpClientBuilder.host
                    this@HttpClientBuilder.port?.let { port = it }
                }

                header(HttpHeaders.Authorization, "Bearer ${tokenProvider()?.let { it } ?: ""}")


            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }


            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }
}
