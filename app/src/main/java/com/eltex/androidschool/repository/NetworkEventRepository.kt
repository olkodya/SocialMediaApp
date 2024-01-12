package com.eltex.androidschool.repository

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.model.Event
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

class NetworkEventRepository : EventRepository {

    private companion object {
        val JSON_TYPE = "application/json".toMediaType()
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor {
            it.proceed(
                it.request()
                    .newBuilder()
                    .addHeader("Api-Key", BuildConfig.API_KEY)
                    .addHeader("Authorization", BuildConfig.AUTH_TOKEN)
                    .build()
            )
        }
        .let {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            } else {
                it
            }
        }
        .build()

    override fun getEvents(callback: com.eltex.androidschool.utils.Callback<List<Event>>) {
        val request = Request.Builder()
            .url("https://eltex-android.ru/api/events")
            .addHeader("Api-Key", BuildConfig.API_KEY)
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun likeById(id: Long, callback: com.eltex.androidschool.utils.Callback<Event>) {
        val request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("https://eltex-android.ru/api/events/$id/likes")
            .build()

        val call = client.newCall(request)
        call.enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun unLikeById(id: Long, callback: com.eltex.androidschool.utils.Callback<Event>) {
        val request = Request.Builder()
            .delete()
            .url("https://eltex-android.ru/api/events/$id/likes")
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun unParticipateById(
        id: Long,
        callback: com.eltex.androidschool.utils.Callback<Event>
    ) {
        val request = Request.Builder()
            .delete()
            .url("https://eltex-android.ru/api/events/$id/participants")
            .build()

        val call = client.newCall(request)
        call.enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )


    }


    override fun participateById(
        id: Long,
        callback: com.eltex.androidschool.utils.Callback<Event>,
    ) {
        val request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("https://eltex-android.ru/api/events/$id/participants")
            .build()


        val call = client.newCall(request)
        call.enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun saveEvent(
        id: Long,
        content: String,
        datetime: String,
        callback: com.eltex.androidschool.utils.Callback<Event>
    ) {
        val request = Request.Builder()
            .post(json.encodeToString(Event(id, content, datetime)).toRequestBody(JSON_TYPE))
            .url("https://eltex-android.ru/api/events")
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun deleteById(
        id: Long,
        callback: com.eltex.androidschool.utils.Callback<Unit>
    ) {
        val request = Request.Builder()
            .delete()
            .url("https://eltex-android.ru/api/events/$id")
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(Unit)
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

}