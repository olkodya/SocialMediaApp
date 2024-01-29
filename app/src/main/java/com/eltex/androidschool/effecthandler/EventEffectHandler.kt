package com.eltex.androidschool.effecthandler

import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventEffect
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventWithError
import com.eltex.androidschool.mvi.EffectHandler
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import java.util.concurrent.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class EventEffectHandler(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper = EventUiModelMapper(),

    ) : EffectHandler<EventEffect, EventMessage> {
    override fun connect(messages: Flow<EventEffect>): Flow<EventMessage> =
        listOf(
            messages.filterIsInstance<EventEffect.LoadInitialPage>()
                .mapLatest {
                    EventMessage.InitialLoaded(
                        try {
                            val result = repository.getLatest(it.count)
                            Either.Right(result.map(mapper::map))

                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(e)
                        }

                    )
                },

            messages.filterIsInstance<EventEffect.LoadNextPage>()
                .mapLatest {
                    EventMessage.NextPageLoaded(
                        try {
                            val result = repository.getBefore(it.id, it.count)
                            Either.Right(result.map(mapper::map))

                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(e)
                        }
                    )
                },
            messages.filterIsInstance<EventEffect.Delete>()
                .mapLatest {
                    try {
                        repository.deleteById(it.event.id)
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        EventMessage.DeleteError(EventWithError(it.event, e))
                    }
                }
                .filterIsInstance<EventMessage.DeleteError>(),

            messages.filterIsInstance<EventEffect.Like>()
                .mapLatest {
                    EventMessage.LikeResult(
                        try {
                            Either.Right(
                                mapper.map(
                                    if (it.event.likedByMe) {
                                        repository.unLikeById(it.event.id)
                                    } else {
                                        repository.likeById(it.event.id)
                                    }
                                )
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(EventWithError(it.event, e))
                        }
                    )
                },


            messages.filterIsInstance<EventEffect.Participate>()
                .mapLatest {
                    EventMessage.ParticipateResult(
                        try {
                            Either.Right(
                                mapper.map(
                                    if (it.event.participatedByMe) {
                                        repository.unParticipateById(it.event.id)
                                    } else {
                                        repository.participateById(it.event.id)
                                    }
                                )
                            )
                        } catch (e: Exception) {
                            if (e is CancellationException) throw e
                            Either.Left(EventWithError(it.event, e))
                        }
                    )
                }
        )
            .merge()

}