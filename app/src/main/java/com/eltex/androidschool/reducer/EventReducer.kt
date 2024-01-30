package com.eltex.androidschool.reducer

import com.eltex.androidschool.model.EventEffect
import com.eltex.androidschool.model.EventMessage
import com.eltex.androidschool.model.EventStatus
import com.eltex.androidschool.model.EventUiState
import com.eltex.androidschool.mvi.Reducer
import com.eltex.androidschool.mvi.ReducerResult
import com.eltex.androidschool.utils.Either

class EventReducer : Reducer<EventUiState, EventEffect, EventMessage> {
    companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_LOAD_SIZE = 3 * PAGE_SIZE
    }

    override fun reduce(
        old: EventUiState,
        message: EventMessage
    ): ReducerResult<EventUiState, EventEffect> = when (message) {
        is EventMessage.Delete -> ReducerResult(
            old.copy(events = old.events.filter { it.id != message.event.id }),
            EventEffect.Delete(message.event)
        )

        is EventMessage.DeleteError -> ReducerResult(
            old.copy(
                events = buildList(old.events.size + 1) {
                    val eventUiModel = message.error.eventUiModel
                    addAll(old.events.takeWhile { it.id > eventUiModel.id })
                    add(eventUiModel)
                    addAll(old.events.takeLastWhile { it.id < eventUiModel.id })
                },
                singleError = message.error.throwable,
            )
        )

        EventMessage.HandleError -> ReducerResult(
            old.copy(singleError = null)
        )

        is EventMessage.InitialLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> if (old.events.isEmpty()) {
                    old.copy(status = EventStatus.EmptyError(result.value))
                } else {
                    old.copy(singleError = result.value)
                }

                is Either.Right ->
                    old.copy(
                        events = result.value,
                        status = EventStatus.Idle(result.value.size < INITIAL_LOAD_SIZE)
                    )
            }
        )


        is EventMessage.Like -> ReducerResult(
            old.copy(events = old.events.map {
                if (it.id == message.event.id) {
                    message.event.copy(
                        likedByMe = !message.event.likedByMe,
                        likes = if (message.event.likedByMe) {
                            message.event.likes - 1
                        } else {
                            message.event.likes + 1
                        }
                    )
                } else {
                    it
                }
            }),
            EventEffect.Like(message.event)
        )

        is EventMessage.LikeResult -> when (val result = message.result) {
            is Either.Left -> ReducerResult(
                old.copy(
                    events = old.events.map {
                        if (it.id == result.value.eventUiModel.id) {
                            result.value.eventUiModel
                        } else {
                            it
                        }
                    },
                    singleError = result.value.throwable
                )
            )

            is Either.Right -> ReducerResult(
                old.copy(
                    events = old.events.map {
                        if (it.id == result.value.id) {
                            result.value
                        } else {
                            it
                        }
                    }
                )
            )
        }

        is EventMessage.Participate -> ReducerResult(
            old.copy(events = old.events.map {
                if (it.id == message.event.id) {
                    it.copy(
                        participatedByMe = !message.event.participatedByMe,
                        participants = if (message.event.participatedByMe) {
                            message.event.participants - 1
                        } else {
                            message.event.participants + 1
                        }
                    )
                } else {
                    it
                }
            }),
            EventEffect.Participate(message.event)
        )

        is EventMessage.ParticipateResult -> when (val result = message.result) {
            is Either.Left -> ReducerResult(
                old.copy(
                    events = old.events.map {
                        if (it.id == result.value.eventUiModel.id) {
                            result.value.eventUiModel
                        } else {
                            it
                        }
                    },
                    singleError = result.value.throwable
                )
            )

            is Either.Right -> ReducerResult(
                old.copy(
                    events = old.events.map {
                        if (it.id == result.value.id) {
                            result.value
                        } else {
                            it
                        }
                    }
                )
            )
        }

        EventMessage.LoadNextPage -> if (old.status is EventStatus.Idle && !old.status.finish) {
            ReducerResult(
                old.copy(status = EventStatus.NextPageLoading),
                EventEffect.LoadNextPage(old.events.last().id, PAGE_SIZE)
            )
        } else {
            ReducerResult(old)
        }

        EventMessage.Retry -> ReducerResult(
            old.copy(status = EventStatus.NextPageLoading),
            EventEffect.LoadNextPage(old.events.last().id, PAGE_SIZE)
        )

        is EventMessage.NextPageLoaded -> ReducerResult(
            when (val result = message.result) {
                is Either.Left -> old.copy(
                    status = EventStatus.NextPageError(result.value),
                )

                is Either.Right -> old.copy(
                    events = old.events + result.value,
                    status = EventStatus.Idle(result.value.size < PAGE_SIZE),
                )
            }

        )

        EventMessage.Refresh -> ReducerResult(
            old.copy(
                status = if (old.events.isEmpty()) {
                    EventStatus.InitialLoading
                } else {
                    EventStatus.Refreshing
                },
            ),
            EventEffect.LoadInitialPage(INITIAL_LOAD_SIZE)
        )
    }
}