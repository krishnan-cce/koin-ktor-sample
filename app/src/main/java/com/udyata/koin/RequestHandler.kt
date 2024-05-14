package com.udyata.koin


import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareRequest
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.content.PartData
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class RequestHandler(val httpClient: HttpClient) {

    suspend inline fun <reified B, reified R> executeRequest(
        method: HttpMethod,
        urlPathSegments: List<Any>,
        body: B? = null,
        queryParams: Map<String, Any>? = null,
        contentType: ContentType = ContentType.Application.Json
    ): RequestState<R> {
        delay(1000L)
        return withContext(Dispatchers.IO) {
            try {
                val response = httpClient.prepareRequest {
                    this.method = method
                    url {
                        val pathSegments = urlPathSegments.map { it.toString() }
                        appendPathSegments(pathSegments)
                    }
                    body?.let {
                        setBody(body)
                        header(HttpHeaders.ContentType, contentType.toString())
                    }
                    queryParams?.let { params ->
                        params.forEach { (key, value) ->
                            parameter(key, value)
                        }
                    }
                }.execute().body<R>()
                RequestState.Success(response)
            } catch (e: Exception) {
                val message = when (e) {
                    is ResponseException -> handleResponseException(e)
                    else -> e.message ?: "Unknown error"
                }
                RequestState.Error(message.toString())
            }
        }
    }
    suspend fun handleResponseException(e: ResponseException): String {
        return withContext(Dispatchers.IO) {
            val errorBody = e.response.bodyAsText()
            val status = e.response.status
            Log.d("Error Status", "Code:${status.value.toString()},Error: $errorBody")

            val gson = Gson()

            when (status) {
                HttpStatusCode.Unauthorized -> {
                    val errorData = parseErrorBody<DefaultError>(errorBody, gson)
                    errorData?.message.toString()
                }
                HttpStatusCode.NotFound -> {
                    val errorData = parseErrorBody<NotFoundErrorResponse>(errorBody, gson)
                    errorData?.message.toString()

                }
                HttpStatusCode.Conflict -> {
                    val errorData = parseErrorBody<ConflictErrorResponse>(errorBody, gson)
                    errorData?.message.toString()

                }
                HttpStatusCode.UnprocessableEntity -> {
                    val errorData = parseErrorBody<UnProcessableContentResponse>(errorBody, gson)
                    errorData?.message.toString()

                }
                else -> status.value.toString()
            }
        }
    }


    private inline fun <reified T> parseErrorBody(jsonString: String, gson: Gson): T? {
        return try {
            gson.fromJson(jsonString, T::class.java)
        } catch (ex: JsonSyntaxException) {
            null
        }
    }


    suspend inline fun <reified R> get(
        urlPathSegments: List<Any>,
        queryParams: Map<String, Any>? = null,
    ): RequestState<R> = executeRequest<Any, R>(
        method = HttpMethod.Get,
        urlPathSegments = urlPathSegments.toList(),
        queryParams = queryParams,
    )

    suspend inline fun <reified B, reified R> post(
        urlPathSegments: List<Any>,
        body: B? = null
    ): RequestState<R> {
        Log.d("RequestHandler", "Preparing to send POST request to: ${urlPathSegments.joinToString("/")}")
        body?.let { Log.d("RequestHandler", "With body: $body") }

        return executeRequest<B, R>(
            method = HttpMethod.Post,
            urlPathSegments = urlPathSegments.toList(),
            body = body
        ).also { result ->
            when (result) {
                is RequestState.Success -> Log.d("RequestHandler", "POST request successful with response: ${result.data}")
                is RequestState.Error -> Log.d("RequestHandler", "POST request failed with error: ${result.message}")
                RequestState.Idle -> TODO()
                RequestState.Loading -> TODO()
            }
        }
    }


    suspend inline fun <reified B, reified R> put(
        urlPathSegments: List<Any>,
        body: B? = null,
    ): RequestState<R> = executeRequest(
        method = HttpMethod.Put,
        urlPathSegments = urlPathSegments.toList(),
        body = body,
    )

    suspend inline fun <reified B, reified R> delete(
        urlPathSegments: List<Any>,
        body: B? = null,
    ): RequestState<R> = executeRequest(
        method = HttpMethod.Delete,
        urlPathSegments = urlPathSegments.toList(),
        body = body,
    )

    suspend inline fun <reified R> postFormUrlEncoded(
        urlPathSegments: List<Any>,
        formParameters: Map<String, Any>
    ): RequestState<R> = executeRequest<Map<String, Any>, R>(
        method = HttpMethod.Post,
        urlPathSegments = urlPathSegments,
        body = formParameters,
        contentType = ContentType.Application.FormUrlEncoded
    )

    suspend inline fun <reified R> postMultipart(
        urlPathSegments: List<Any>,
        formDataParts: List<PartData>
    ): RequestState<R> = executeRequest<List<PartData>, R>(
        method = HttpMethod.Post,
        urlPathSegments = urlPathSegments,
        body = formDataParts,
        contentType = ContentType.MultiPart.FormData
    )
}


@Serializable
data class DefaultError(
    @SerialName("message")
    val message: String,
)


data class ConflictErrorResponse(
    val status: Boolean,
    val message: String
)
data class UnProcessableContentResponse(
    val data: Any?,
    val message: String,
    val status: Boolean
)
data class NotFoundErrorResponse(
    val message: String,
    val timestamp: String,
    val details:String
)
