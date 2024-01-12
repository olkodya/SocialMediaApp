package com.eltex.androidschool.repository

import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkEventRepository(
    private val api: EventsApi
) : EventRepository {
    override fun getEvents(callback: com.eltex.androidschool.utils.Callback<List<Event>>) {
        api.getAll().enqueue(
            object : Callback<List<Event>> {
                override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                    callback.onError(t)
                }

            }
        )
    }

    override fun likeById(id: Long, callback: com.eltex.androidschool.utils.Callback<Event>) {
        api.like(id).enqueue(
            object : Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun unLikeById(id: Long, callback: com.eltex.androidschool.utils.Callback<Event>) {
        api.unlike(id).enqueue(
            object : Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun unParticipateById(
        id: Long,
        callback: com.eltex.androidschool.utils.Callback<Event>
    ) {
        api.unparticipate(id).enqueue(
            object : Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )


    }


    override fun participateById(
        id: Long,
        callback: com.eltex.androidschool.utils.Callback<Event>,
    ) {
        api.participate(id).enqueue(
            object : Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
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
        api.save(Event(id, content, datetime)).enqueue(
            object : Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = requireNotNull(response.body())
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

    override fun deleteById(
        id: Long,
        callback: com.eltex.androidschool.utils.Callback<Unit>
    ) {
        api.delete(id).enqueue(
            object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(t)
                }
            }
        )
    }

}